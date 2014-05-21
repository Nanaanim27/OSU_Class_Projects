import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.OperationStatus;


public class Insert {
	
	private MyDbs mydbs;
    // DatabaseEntries used for loading records
    private static DatabaseEntry theKey = new DatabaseEntry();
    private static DatabaseEntry theData = new DatabaseEntry();

    
	public Insert(MyDbs mydbs)
	{
		this.mydbs = mydbs;
	}
	public void insertRU(ArrayList<Integer> keys, char dbChar)
	{
		TupleBinding tuplerubinding = new TupleRUBinding();
		int counter = 0;
		for(int i = 0; i < keys.size(); i++)
		{
			byte[] key = new byte[4];
			int x = keys.get(i);
			key[0] = (byte) (x >> 24);
	    	key[1] = (byte) (x >> 16);
	    	key[2] = (byte) (x >> 8);
	    	key[3] = (byte) (x /*>> 0*/);
	    	
			byte[] arr = new byte[8000];
			Random rand = new Random();
			int j = rand.nextInt();
			arr[7996] = (byte) (j >> 24);
	    	arr[7997] = (byte) (j >> 16);
	    	arr[7998] = (byte) (j >> 8);
	    	arr[7999] = (byte) (j /*>> 0*/);
			
			theKey = new DatabaseEntry(key);

			TupleRU tru = new TupleRU();
			tru.setPrimaryKey(keys.get(i));
			tru.setEightKB(arr);
        
			// Place the Vendor object on the DatabaseEntry object using our
			// the tuple binding we implemented in InventoryBinding.java
			tuplerubinding.objectToEntry(tru, theData);

			// Put it in the database. Note that this causes our secondary database
			// to be automatically updated for us.
			try {
				if(dbChar == 'r') {
					mydbs.getTableR().put(null, theKey, theData);
					counter++;
				}
				else if (dbChar == 'u') {
					mydbs.getTableU().append(null, theKey, theData);
					counter++;
				}
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				System.out.println("inserting into database S tuples");
				e.printStackTrace();
			}
		}
		System.out.println("Inserted " + counter + " entries into " + dbChar);
	}
	public void insertS(ArrayList<Integer> xs, ArrayList<Integer> ys)
	{
		TupleBinding tuplerubinding = new TupleSBinding();
		
		
			
		for(int i = 0; i < xs.size(); i++)
		{
			byte[] key = new byte[4];
			int x = xs.get(i);
			key[0] = (byte) (x >> 24);
	    	key[1] = (byte) (x >> 16);
	    	key[2] = (byte) (x >> 8);
	    	key[3] = (byte) (x /*>> 0*/);
	    	
			theKey = new DatabaseEntry(key);

			TupleS trs = new TupleS();
			trs.setX(xs.get(i));
			trs.setY(ys.get(i));
        
			// Place the Vendor object on the DatabaseEntry object using our
			// the tuple binding we implemented in InventoryBinding.java
			tuplerubinding.objectToEntry(trs, theData);

			// Put it in the database. Note that this causes our secondary database
			// to be automatically updated for us.
			try {
				OperationStatus s;
				s = mydbs.getTableS().append(null, theKey, theData);
				
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				System.out.println("Found the error");
				e.printStackTrace();
			}
		}
	}
	public void insertWXYZ(ArrayList<TupleWXYZ> tpls)
	{
		TupleBinding tuplerubinding = new TupleSBinding();
		Database db = mydbs.getTableS();
		
			
		for(int i = 0; i < tpls.size(); i++)
		{
			String key = String.valueOf(tpls.get(i).getX());
			
			theKey = new DatabaseEntry(key.getBytes());

			
			// Place the Vendor object on the DatabaseEntry object using our
			// the tuple binding we implemented in InventoryBinding.java
			tuplerubinding.objectToEntry(tpls.get(i), theData);

			// Put it in the database. Note that this causes our secondary database
			// to be automatically updated for us.
			try {
				db.put(null, theKey, theData);
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				System.out.println("db exception inserting wxyz tuple");
				e.printStackTrace();
			}
		}
		db = null;
	}
	
}
