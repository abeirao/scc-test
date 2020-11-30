package scc.serverless;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.http.protocol.ExecutionContext;

import com.microsoft.azure.functions.annotation.*;

import redis.clients.jedis.Jedis;
import scc.data.Messsage;
import scc.data.Forum;
import scc.data.CosmosDBLayer;
import scc.data.Calendar;
import scc.redis.RedisCache;
import scc.srv.CosmosDBFactory;
import scc.utils.AzureProperties;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer Trigger.
 */
public class TimerFunction {
    static int count = 0;

    @FunctionName("periodic-compute")
    public void updateAvailableDays(
            @TimerTrigger(name = "keepAliveTrigger", schedule = "0 0 */24 * * *") String timerInfo,
            ExecutionContext context) {

        CosmosContainer calendars = CosmosDBFactory.getCosmosClient()
                .getDatabase(AzureProperties.getProperty(AzureProperties.COSMOSDB_DATABASE)).getContainer("calendars");

        // get all calendars in database
        CosmosPagedIterable<Calendar> it = calendars.queryItems("SELECT * FROM Calendar c",
                new CosmosQueryRequestOptions(), Calendar.class);

        // update available days in each calendar
        for (Calendar calendar : it) {
            List<Date> availableDays = calendar.getAvailableDays();
            // subtract the day that passed
            calendar.setAvailableDays(availableDays.subList(1, availableDays.size() - 1));
            // update calendar in database
            calendars.upsertItem(calendar);
        }
    }
}
