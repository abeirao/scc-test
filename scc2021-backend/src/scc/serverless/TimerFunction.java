package scc.serverless;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.protocol.ExecutionContext;

import com.microsoft.azure.functions.annotation.*;

import redis.clients.jedis.Jedis;
import scc.data.ForumMessage;
import scc.data.Forum;
import scc.data.CosmosDBLayer;
import scc.redis.RedisCache;
import scc.utils.AzureProperties;

import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer Trigger.
 */
public class TimerFunction {
	static int count = 0;
	
	// TODO function to process and update all listed entities in the cache
    
    @FunctionName("periodic-compute")
    public void cosmosFunction( @TimerTrigger(name = "keepAliveTrigger", schedule = "0 0 */24 * * *") String timerInfo,
          ExecutionContext context) {
    	synchronized(HttpFunction.class) {
    		HttpFunction.count++;
    	}
		try (Jedis jedis = RedisCache.getCachePool().getResource()) {
			jedis.set("serverlesstime", new SimpleDateFormat().format(new Date()));
			try {
				
				CosmosPagedIterable<ForumMessage> it = CosmosDBLayer.getInstance().getDatabase(AzureProperties.getProperty(AzureProperties.COSMOSDB_DATABASE))
						.getContainer("Forum").queryItems("SELECT * FROM Forum f ORDER BY f.creationTime DESC OFFSET 0 LIMIT 20",
								new CosmosQueryRequestOptions(), ForumMessage.class);

				List<ForumMessage> lst = new ArrayList<ForumMessage>();
				it.stream().forEach( m -> lst.add(m));

				jedis.set("serverless:cosmos", new ObjectMapper().writeValueAsString(lst));
			} catch (Exception e) {
				jedis.set("serverless:cosmos", "[]");
			}
		}
    }
}
