package scc.search;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.microsoft.azure.management.Azure;
import com.microsoft.azure.management.search.QueryKey;
import com.microsoft.azure.management.search.SearchService;

/**
 * This class dumps the information for a given search index in the format that
 * will be used by the Search program.
 */
public class SearchDumpProperties
{
	// Auth file location
	// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11
	// This file should be created by running in the console:
	// az ad sp create-for-rbac --sdk-auth > azure.auth
	static final String AZURE_AUTH_LOCATION = "azure.auth";
	public static final String SEARCH_PROP_FILE = "azure-search.props";
	public static final String SEARCH_SERVICE = "scccs50415";

	public static void main(String[] args) {
		String propFile = SEARCH_PROP_FILE;
		String searchService = SEARCH_SERVICE;
		if (args.length > 0) {
			searchService = args[0];
			if (args.length > 1) {
				propFile = args[1];
			}
		}
		try {
			Azure azure = AzureManagementUtils.cretaeManagementClient(AZURE_AUTH_LOCATION);
			for (SearchService srv : azure.searchServices().list()) {
				if (!srv.name().equalsIgnoreCase(searchService))
					continue;
				Files.deleteIfExists(Paths.get(propFile));
				Files.write(Paths.get(propFile),
						("# Date : " + new SimpleDateFormat().format(new Date()) + "\n").getBytes(),
						StandardOpenOption.CREATE, StandardOpenOption.WRITE);
				Files.write(Paths.get(propFile), ("SearchServiceName=" + searchService + "\n").getBytes(),
						StandardOpenOption.APPEND);
				Files.write(Paths.get(propFile),
						("SearchServiceAdminKey=" + srv.getAdminKeys().primaryKey() + "\n").getBytes(),
						StandardOpenOption.APPEND);

				List<QueryKey> lst = srv.listQueryKeys();
				if (lst == null || lst.size() == 0)
					srv.createQueryKey("newquerykey");
				lst = srv.listQueryKeys();
				Files.write(Paths.get(propFile), ("SearchServiceQueryKey=" + lst.get(0).key() + "\n").getBytes(),
						StandardOpenOption.APPEND);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
