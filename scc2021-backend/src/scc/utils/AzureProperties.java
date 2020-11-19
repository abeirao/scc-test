package scc.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AzureProperties
{
	public static final String BLOB_KEY = "DefaultEndpointsProtocol=https;AccountName=scc50415;AccountKey=fR4+F2fg/uozoauqf2iWJRPvyyqMj1wqjGB/52N07mkOQx0btUy90EGt1CL5luMAIrZn0p/CTvMCIc5eNoB7/w==;EndpointSuffix=core.windows.net";
	public static final String COSMOSDB_KEY = "bbgF4Re4UQMuEsX0MZSEjuLDCZdMU76srR5VLAnaluK5QuXKcZUeKeFPEp8mMAgZwTMcAUz6T8oW61WTIh5ymg==";
	public static final String COSMOSDB_URL = "https://scc-cosmos-50415.documents.azure.com:443/";
	public static final String COSMOSDB_DATABASE = "scc50415p";

	public static final String PROPS_FILE = "azurekeys-westeurope.props";
	private static Properties props;
	
	public static synchronized Properties getProperties() {
		if( props == null) {
			props = new Properties();
			try {
				props.load( new FileInputStream(PROPS_FILE));
			} catch (IOException e) {
				// do nothing
			}
		}
		return props;
	}

	public static String getProperty( String key) {
		try {
			String val = System.getenv( key);
			if( val != null)
				return val;
		} catch( Exception e) {
			// do nothing
		}
		return getProperties().getProperty(key);		
	}


}
