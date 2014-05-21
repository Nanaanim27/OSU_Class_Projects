import java.io.File;
import java.io.FileNotFoundException;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import com.sleepycat.db.SecondaryConfig;
import com.sleepycat.db.SecondaryDatabase;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import com.sleepycat.persist.StoreConfig;




public class Mydbs {

	  /** The EntityStore for our Contact database. */
	  private static EntityStore store;
	  
	  /** The PrimaryIndex accessor for contacts. */
	  private static PrimaryIndex<Integer, XmlFile> nameIndex; 
	  private static SecondaryIndex<Long, Integer, XmlFile> sizeIndex;
	  
	  /** Initialize the static variables at class load time to ensure there's only one of them. */
	  static {
	    // Create the directory in which this store will live.
	    String currDir = "./";
	    File dir = new File(currDir, "xmldb");
	    boolean success = dir.mkdirs();
	    if (success) {
	      System.out.println("Created the xmldb directory.");
	    }
	    try
	    {
	    	
		    EnvironmentConfig envConfig = new EnvironmentConfig();
		    StoreConfig storeConfig = new StoreConfig();
		    //envConfig.setCacheSize(4*1024*1024);
		    envConfig.setInitializeCache(true);
		    envConfig.setErrorPrefix("MyDbs");
		    envConfig.setErrorStream(System.err);
		    envConfig.setAllowCreate(true);
		    storeConfig.setAllowCreate(true);
	        
		    System.out.println(dir.getPath());
		    Environment env = new Environment(dir,  envConfig);
		    Mydbs.store = new EntityStore(env, "EntityStore", storeConfig);
		    nameIndex = store.getPrimaryIndex(Integer.class, XmlFile.class);
		    sizeIndex = store.getSecondaryIndex(nameIndex, Long.class, "fileSize");
		    DbShutdownHook shutdownHook = new DbShutdownHook(env, store);
		    Runtime.getRuntime().addShutdownHook(shutdownHook);
		    
		    
	    } catch (DatabaseException dbe)
	    {
	    	dbe.printStackTrace();	    	
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	  }
	
    // Our constructor does nothing
    public Mydbs() {}

    

   // getter methods

    public EntityStore getStore() {
        return store;
    }

    public SecondaryIndex getFileSizeIndex() {
        return sizeIndex;
    }

    public PrimaryIndex getFileNameIndex() {
        return nameIndex;
    }
}
