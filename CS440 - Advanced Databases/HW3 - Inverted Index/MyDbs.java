/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2004, 2012 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$ 
 */

// File: MyDbs.java



import java.io.FileNotFoundException;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.SecondaryConfig;
import com.sleepycat.db.SecondaryDatabase;


public class MyDbs {
	
    // The databases that our application uses
    private Database invertedDb = null;
    private String inverteddb = "inverted.db";
    private Database classCatalogDb = null;
    private String classCatalogdb = "classCatalog.db";
    
    private StoredClassCatalog classCatalog = null;
    // Our constructor does nothing
    public MyDbs() {}

    // The setup() method opens all our databases
    // for us.
    public void setup(String databasesHome)
        throws DatabaseException {
    	try {

    		DatabaseConfig myDbConfig = new DatabaseConfig();
    		myDbConfig.setErrorStream(System.err);
    		myDbConfig.setErrorPrefix("MyDbs");
    		myDbConfig.setType(DatabaseType.BTREE);
    		myDbConfig.setAllowCreate(true);


    		// Now open, or create and open, our databases
    		// Open the vendors and inventory databases

    		inverteddb = databasesHome + "/" + inverteddb;
    		invertedDb = new Database(inverteddb, null, myDbConfig);
    		
    		classCatalogdb = databasesHome + "/" + classCatalogdb;
    		classCatalogDb = new Database(classCatalogdb, null, myDbConfig);
    		
    		classCatalog = new StoredClassCatalog(classCatalogDb);

        } catch(FileNotFoundException fnfe) {
            System.err.println("MyDbs: " + fnfe.toString());
            System.exit(-1);
        }

        

        // Need a tuple binding for the Inventory class.
        // We use the InventoryBinding class
        // that we implemented for this purpose.
        //TupleBinding keywordbingind = new KeyWordBinding();

    }

   // getter methods

    public Database getInvertedDB() {
        return invertedDb;
    }
    
    public StoredClassCatalog getClassCatalog() {
       	return classCatalog;
    }
  
    // Close the databases
    public void close() {
        try {
        	if (classCatalogDb != null) {
                classCatalogDb.close();
            }
        	if (invertedDb != null) {
                invertedDb.close();
            }
        } catch(DatabaseException dbe) {
            System.err.println("Error closing MyDbs: " +
                                dbe.toString());
            System.exit(-1);
        }
    }
}

