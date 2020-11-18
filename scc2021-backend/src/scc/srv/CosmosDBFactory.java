package scc.srv;

import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;

import scc.utils.AzureProperties;

public class CosmosDBFactory
{
	private static CosmosClient client;
	
	public static synchronized CosmosClient getCosmosClient( ) {
		if( client != null)
				return client;
		client = new CosmosClientBuilder()
			         .endpoint(AzureProperties.getProperty(AzureProperties.COSMOSDB_URL))
			         .key(AzureProperties.getProperty(AzureProperties.COSMOSDB_KEY))
			         .directMode()		// comment this is not to use direct mode
			         .consistencyLevel(ConsistencyLevel.SESSION)
			         .connectionSharingAcrossClientsEnabled(true)
			         .contentResponseOnWriteEnabled(true)
			         .buildClient();
			return client;
			
		}
}
