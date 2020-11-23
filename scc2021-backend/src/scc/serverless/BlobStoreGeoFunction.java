package scc.serverless;

import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import redis.clients.jedis.Jedis;
import scc.redis.RedisCache;
import scc.srv.api.services.MediaService;
import scc.utils.Hash;

import com.microsoft.azure.functions.*;

/**
 * Azure Functions with Timer Trigger.
 */
public class BlobStoreGeoFunction {
	
	private static final String STORAGE_CONNECTION_STRING_USA = "DefaultEndpointsProtocol=https;AccountName=scc5018;AccountKey=qsC32DUOSHRcaaoBBM5bWOTXEOd8ZScVdFvnojYT6P6lu4hw366FpvyACqu9uPbZi2XTsj1gWOmFlS1+OKuJ2Q==;EndpointSuffix=core.windows.net";
	private static final String STORAGE_CONNECTION_STRING_EUA = "DefaultEndpointsProtocol=https;AccountName=scc50415;AccountKey=fR4+F2fg/uozoauqf2iWJRPvyyqMj1wqjGB/52N07mkOQx0btUy90EGt1CL5luMAIrZn0p/CTvMCIc5eNoB7/w==;EndpointSuffix=core.windows.net";
	static int count = 0;

    @FunctionName("blobgeo")
	public void run(
			@BlobTrigger(name = "blob", dataType = "binary", path = "images/{name}", connection = STORAGE_CONNECTION_STRING_EUA) byte[] content,
			@BindingName("name") String blobname, final ExecutionContext context) {
		try {
			String id = Hash.of(content);
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING_USA);
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
			CloudBlobContainer container = blobClient.getContainerReference("images");
			CloudBlob blob = container.getBlockBlobReference(id);
			blob.uploadFromByteArray(content, 0, content.length);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}