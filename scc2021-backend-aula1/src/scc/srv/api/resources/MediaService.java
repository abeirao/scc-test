package scc.srv.api.resources;

import java.io.ByteArrayOutputStream;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import scc.srv.api.MediaAPI;
import scc.utils.Hash;

/** 
 * Media service for the reservations service
 * Uses blob storage to store media files
 */
public class MediaService implements MediaAPI {
	
	// azure cloud storage account connection string
	private static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=scc50415;AccountKey=fR4+F2fg/uozoauqf2iWJRPvyyqMj1wqjGB/52N07mkOQx0btUy90EGt1CL5luMAIrZn0p/CTvMCIc5eNoB7/w==;EndpointSuffix=core.windows.net";
	
	private CloudBlobContainer container = null;
		
	synchronized CloudBlobContainer init() {
		try {
			if (container == null) {
				CloudStorageAccount storageAccount = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
				CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
				container = blobClient.getContainerReference("images");
			}
			return container;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String upload(byte[] contents) {
		try {
			String id = Hash.of(contents);
			CloudBlob blob = init().getBlockBlobReference(id);
			blob.uploadFromByteArray(contents, 0, contents.length);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public byte[] download(String id) {
		try {
			CloudBlob blob = init().getBlobReferenceFromServer(id);
		
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			blob.download(out);
			out.close();
			
			byte[] contents = out.toByteArray();
			
			return contents;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String delete(String id) {
		try {
			CloudBlob blob = init().getBlobReferenceFromServer(id);
			blob.delete();
		
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
