import java.io.FileNotFoundException;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;

public class MyDbs {

	private Database tableR = null;
	private String tabler = "tableR.db";
	private Database tableS = null;
	private String tables = "tableS.db";
	private Database tableU = null;
	private String tableu = "tableU.db";
	private Database tableRSU = null;
	private String tablersu = "tablersu.db";

	public void setup(String databasesHome) throws DatabaseException {
		try {

			DatabaseConfig rConfig = new DatabaseConfig();
			rConfig.setErrorStream(System.err);
			rConfig.setErrorPrefix("MyDbsR");
			rConfig.setType(DatabaseType.BTREE);
			rConfig.setAllowCreate(true);
			rConfig.setSortedDuplicates(true);
			
			DatabaseConfig myDbConfig = new DatabaseConfig();
			myDbConfig.setErrorStream(System.err);
			myDbConfig.setErrorPrefix("MyDbsR");
			myDbConfig.setType(DatabaseType.HEAP);
			myDbConfig.setAllowCreate(true);
			myDbConfig.setHeapsize(32768000); // Configure the Heap file size
			
			tabler = databasesHome + "./" + tabler;
			tableR = new Database(tabler, null, rConfig);
			tables =  "./" + tables;
			tableS = new Database(tables, null, myDbConfig);
			tableu = databasesHome + "./" + tableu;
			tableU = new Database(tableu, null, myDbConfig);
			//tablersu = databasesHome + "./" + tablersu;
			//tableRSU = new Database(tablersu, null, myDbConfig);

		} catch (FileNotFoundException fnfe) {
			System.err.println("MyDbs: " + fnfe.toString());
			System.exit(-1);
		}

	}

	
	
	public Database getTableR() {
        return tableR;
    }
	
	public Database getTableS() {
        return tableS;
    }
	
	public Database getTableU() {
        return tableU;
    }
}
