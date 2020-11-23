package scc.serverless;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import redis.clients.jedis.Jedis;
import scc.redis.RedisCache;
import scc.utils.Hash;

import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer Trigger.
 */
public class BlobStoreGeoFunction {
	
	private static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=scc50816;AccountKey=B1COa5uOGzIN2q0uyEdNQ3QLITtJa0gBhYFqw9EWrwU4Buccc4atVZvWuSxNJ0LnWbrzOW7ui0P1pvrOHQ+qKw==;EndpointSuffix=core.windows.net";
	static int count = 0;

    @FunctionName("blobgeo")
	public void run(
			@BlobTrigger(name = "blob", dataType = "binary", path = "images/{name}", connection = "BlobStoreConnection") byte[] content,
			@BindingName("name") String blobname, final ExecutionContext context) {
		try {
			String id = Hash.of(content);
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			CloudBlobContainer container = blobClient.getContainerReference("images");
			CloudBlob blob = container.getBlockBlobReference(id);
			blob.uploadFromByteArray(content, 0, content.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}