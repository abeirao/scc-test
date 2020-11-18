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
import scc.srv.CosmosDBFactory;
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
	
	// TODO function to process available days each 24hours 
    
    @FunctionName("periodic-compute")
    public void cosmosFunction( @TimerTrigger(name = "keepAliveTrigger", schedule = "0 0 */24 * * *") String timerInfo,
          ExecutionContext context) {
    	synchronized(HttpFunction.class) {
    		HttpFunction.count++;
    	}
    
		CosmosPagedIterable<Calendar> it = CosmosDBFactory.getCosmosClient().getDatabase(AzureProperties.getProperty(AzureProperties.COSMOSDB_DATABASE))
				.getContainer("calendars").queryItems("SELECT * FROM Calendar c",
						new CosmosQueryRequestOptions(), Calendar.class);
		
		for (Calendar calendar: it) {
			
		}
		
    }
}
