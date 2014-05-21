/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2004, 2012 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$ 
 */

// File: ExampleDatabaseRead

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.Cursor;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;
import com.sleepycat.db.SecondaryCursor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

public class DatabaseRead {

	// Encapsulates the database environment and databases.
	private static MyDbs myDbs = new MyDbs();

	private static EntryBinding xmlFileBinding; 

	public ArrayList<Integer> getMatches(String[] args, String pathName) throws DatabaseException {

		myDbs.setup(pathName);
		xmlFileBinding = new SerialBinding(myDbs.getClassCatalog(), KeyWords.class);
		String myDbsPath = "./";
		Cursor secCursor = null;

		ArrayList<Integer> newTemp = new ArrayList<Integer>();
		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<Integer> values = new ArrayList<Integer>();

		values = getFileNames(args[0], xmlFileBinding);
		
		for (int x = 1; x < args.length; x++) {
			
			String file = args[x];

			try {

				temp = getFileNames(args[x], xmlFileBinding);
				newTemp = new ArrayList<Integer>();
				for (int i = 1; i < values.size(); i++) {
					int hasFileName = Collections.binarySearch(temp, values.get(i));
					
					if (hasFileName >= 0) {
						newTemp.add(values.get(i));

					}
					values =  newTemp;
				}
			} catch (Exception e) {
				System.err.println("Error on inventory secondary cursor:");
				System.err.println(e.toString());
				e.printStackTrace();

			} finally {
				if (secCursor != null) {

					secCursor.close();
				}
			}

		}
		
		return values;

	}
	private ArrayList<Integer> getFileNames(String kw, EntryBinding eb)
	{
		
		Cursor secCursor = null;
		ArrayList<Integer> values = new ArrayList<Integer>();
		try
		{
			
		
		DatabaseEntry searchKey1 = new DatabaseEntry(kw.getBytes("UTF-8"));

		DatabaseEntry foundData = new DatabaseEntry();

		secCursor = myDbs.getInvertedDB().openCursor(null, null);

		System.out.println();

		if (secCursor.getSearchKey(searchKey1, foundData, null) == OperationStatus.SUCCESS) {

			KeyWords theFile = (KeyWords) eb.entryToObject(foundData);
			System.out.println(theFile.getWord());

				values = theFile.getFiles();

		}
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return values;
	}
	

	protected DatabaseRead() {
	}

}
