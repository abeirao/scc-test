package scc.search;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Map.Entry;
import java.util.Properties;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Based on the code from:
 * https://docs.microsoft.com/en-us/azure/search/search-get-started-java
 */
public class AdvancedSearch
{
	public static final String SEARCH_PROP_FILE = "azure-search.props";
	public static final String PROP_SERVICE_NAME = "SearchServiceName";
	public static final String PROP_QUERY_KEY = "SearchServiceQueryKey";

	private static Properties loadProperties(String propsFile) throws IOException {
		Properties props = new Properties();
		props.load(new FileReader(propsFile));
		return props;
	}
	public static void main(String[] args) {
/*		ObjectMapper mapper = new ObjectMapper();
		ObjectNode obj = mapper.createObjectNode();
		obj.put("count", "true");
		obj.put("search", "labore");
*/		String propFile = SEARCH_PROP_FILE;
		if (args.length > 0) {
			propFile = args[0];
		}
		Properties props;
		try {
			props = loadProperties(propFile);

			String serviceName = props.getProperty(PROP_SERVICE_NAME);
			String queryKey = props.getProperty(PROP_QUERY_KEY);

			String hostname = "https://" + serviceName + ".search.windows.net/";
			ClientConfig config = new ClientConfig();
			Client client = ClientBuilder.newClient(config);

			URI baseURI = UriBuilder.fromUri(hostname).build();

			WebTarget target = client.target(baseURI);

			String index = "cosmosdb-index";

			// SIMPLE QUERY
			// Check parameters at: https://docs.microsoft.com/en-us/rest/api/searchservice/search-documents
			JsonObject obj = new JsonObject();
			obj.addProperty("count", "true");
			obj.addProperty("search", "Quaerat");

			String resultStr = target.path("indexes/" + index + "/docs/search").queryParam("api-version", "2019-05-06")
					.request().header("api-key", queryKey)
					.accept(MediaType.APPLICATION_JSON).post(Entity.entity(obj.toString(), MediaType.APPLICATION_JSON))
					.readEntity(String.class);
			JsonObject resultObj = new Gson().fromJson(resultStr, JsonObject.class);
			System.out.println( "Number of results : " + resultObj.get("@odata.count").getAsInt());
			for( JsonElement el: resultObj.get("value").getAsJsonArray()) {
				System.out.println();
				JsonObject elObj = el.getAsJsonObject();
				for( Entry<String, JsonElement> val : elObj.entrySet()) {
					System.out.println( val.getKey() + "->" + val.getValue());
				}
			}
			
			
			// NOT SO SIMPLE QUERY 1
			obj = new JsonObject();
			obj.addProperty("count", "true");
			obj.addProperty("search", "Quaerat");
			obj.addProperty("searchFields", "msg");
			obj.addProperty("select", "rid,entityId,fromWho,msg");
			obj.addProperty("filter", "fromWho eq 'Albert Lind'");

			resultStr = target.path("indexes/" + index + "/docs/search").queryParam("api-version", "2019-05-06")
					.request().header("api-key", queryKey)
					.accept(MediaType.APPLICATION_JSON).post(Entity.entity(obj.toString(), MediaType.APPLICATION_JSON))
					.readEntity(String.class);
			
			System.out.println( "----->" + resultStr);
			resultObj = new Gson().fromJson(resultStr, JsonObject.class);
			System.out.println( "Number of results : " + resultObj.get("@odata.count").getAsInt());
			for( JsonElement el: resultObj.get("value").getAsJsonArray()) {
				System.out.println();
				JsonObject elObj = el.getAsJsonObject();
				for( Entry<String, JsonElement> val : elObj.entrySet()) {
					System.out.println( val.getKey() + "->" + val.getValue());
				}
			}
			
			// NOT SO SIMPLE QUERY 2
			obj = new JsonObject();
			obj.addProperty("count", "true");
			obj.addProperty("search", "Swift");
			obj.addProperty("searchFields", "msg,fromWho");
			obj.addProperty("searchMode", "all");
			obj.addProperty("queryType", "full");
			obj.addProperty("select", "rid,entityId,fromWho,msg");

			resultStr = target.path("indexes/" + index + "/docs/search").queryParam("api-version", "2019-05-06")
					.request().header("api-key", queryKey)
					.accept(MediaType.APPLICATION_JSON).post(Entity.entity(obj.toString(), MediaType.APPLICATION_JSON))
					.readEntity(String.class);
			
			System.out.println( "----->" + resultStr);
			resultObj = new Gson().fromJson(resultStr, JsonObject.class);
			System.out.println( "Number of results : " + resultObj.get("@odata.count").getAsInt());
			for( JsonElement el: resultObj.get("value").getAsJsonArray()) {
				System.out.println();
				JsonObject elObj = el.getAsJsonObject();
				for( Entry<String, JsonElement> val : elObj.entrySet()) {
					System.out.println( val.getKey() + "->" + val.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
