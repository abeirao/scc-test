package scc.srv.api.services;

import java.io.*;
import java.nio.file.Files;

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
public class MediaService {

	String BASE_DIR = "/mnt/vol";


	// azure cloud storage account connection string
	//private static final String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=scc50415;AccountKey=VRcAElcwZSp03PZbo1bfDmQpLuqJK2Ukrsa7rR2bkADZTTDsuYiO1NRsM987oYLYy6DisVVK2cD5PsGStDQGqg==;EndpointSuffix=core.windows.net";

	//private CloudBlobContainer container = null;

/*	synchronized CloudBlobContainer init() {
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
*/

	public String upload(byte[] contents) {
		/**try {
			String id = Hash.of(contents);
			CloudBlob blob = init().getBlockBlobReference(id);
			blob.uploadFromByteArray(contents, 0, contents.length);
			return id;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}*/
		String id = Hash.of(contents);

		File f = new File(BASE_DIR + id);

		if(f.exists() && !f.isDirectory()){
			return id;
		}

		try(FileOutputStream writer = new FileOutputStream(f)){

			writer.write(contents);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
return id;
	}

	public byte[] download(String id) throws IOException {
	/*	try {
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

*/
		return Files.readAllBytes(new File(BASE_DIR + id).toPath());


	}

/*
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
 */
}
