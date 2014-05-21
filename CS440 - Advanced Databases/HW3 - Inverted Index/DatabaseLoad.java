/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2004, 2012 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$ 
 */

// File: ExampleDatabaseLoad.java



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.db.Cursor;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;

public class DatabaseLoad {

	// Encapsulates the databases.
	private static MyDbs myDbs = new MyDbs();
	public static final String delimiter = " \t\n\r\f,.;:/\\\'\"`-_(){}[]?!<>|";
	public static final String[] stopwords = new String[] {"a","about","above","across","after","again","against","all","almost","alone","along","already","also","although","always","among","an","and","another","any","anybody","anyone","anything","anywhere","are","area","areas","around","as","ask","asked","asking","asks","at","away","b","back","backed","backing","backs","be","became","because","become","becomes","been","before","began","behind","being","beings","best","better","between","big","both","but","by","c","came","can","cannot","case","cases","certain","certainly","clear","clearly","com","come","could","d","did","differ","different","differently","do","does","done","down","down","downed","downing","downs","during","e","each","early","either","end","ended","ending","ends","enough","even","evenly","ever","every","everybody","everyone","everything","everywhere","f","face","faces","fact","facts","far","felt","few","find","finds","first","for","four","from","full","fully","further","furthered","furthering","furthers","g","gave","general","generally","get","gets","give","given","gives","go","going","good","goods","got","great","greater","greatest","group","grouped","grouping","groups","h","had","has","have","having","he","her","here","herself","high","high","high","higher","highest","him","himself","his","how","however","http","https","i","if","imdb","important","in","interest","interested","interesting","interests","into","is","it","its","itself","j","just","k","keep","keeps","kind","knew","know","known","knows","l","large","largely","last","later","latest","least","less","let","lets","like","likely","long","longer","longest","m","made","make","making","man","many","may","me","member","members","men","might","more","most","mostly","mr","mrs","much","must","my","myself","n","necessary","need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody","non","noone","ot","nthing","no","nowhre","numbe","numbers","o","of","off","often","old","older","oldest","on","once","one","only","open","opened","opening","opens","or","order","ordered","ordering","orders","other","others","our","out","over","p","part","parted","parting","parts","per","perhaps","place","places","point","pointed","pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","q","quite","r","rather","really","right","right","room","rooms","s","said","same","saw","say","says","second","seconds","see","seem","seemed","seeming","seems","sees","several","shall","she","should","show","showed","showing","shows","side","sides","since","small","smaller","smallest","so","some","somebody","someone","something","somewhere","state","states","still","still","such","sure","t","take","taken","than","that","the","their","them","then","there","therefore","these","they","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to","today","together","too","took","toward","turn","turned","turning","turns","two","u","under","until","up","upon","us","use","used","uses","v","very","w","want","wanted","wanting","wants","was","way","ways","we","well","wells","went","were","what","when","where","whether","which","while","who","whole","whose","why","will","with","within","without","work","worked","working","works","would","www","x","y","year","years","yet","you","young","younger","youngest","your","yours","z"};
	// DatabaseEntries used for loading records
	private static DatabaseEntry theKey = new DatabaseEntry();
	private static DatabaseEntry theData = new DatabaseEntry();
	private int dircounter = 0;
	public void run(String myDbsPath, String filepath)
	{
		try {
			myDbs.setup(myDbsPath);

			System.out.println("loading imdb db....");
			createInvertedIndex(filepath);

		} catch (DatabaseException dbe) {
			System.err.println("DatabaseLoad: " + dbe.toString());
			dbe.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception: " + e.toString());
			e.printStackTrace();
		} finally {
			myDbs.close();
		}
		System.out.println("All done.");

	}


	private void createInvertedIndex(String filePath)
	{

		EntryBinding keywordbinding = new SerialBinding(myDbs.getClassCatalog(), KeyWords.class);

		ArrayList<KeyWords> keywords = new ArrayList<KeyWords>();

		handleDirectory(filePath, keywords, keywordbinding);

		if(keywords.size() > 0)
			addToDB(keywords, keywordbinding);
	}
	private ArrayList<KeyWords> handleDirectory(String filePath, ArrayList<KeyWords> keywords, EntryBinding keywordbinding)
	{
		File f = new File(filePath);
		File[] dirs = f.listFiles();
		int dircount = 0;
		for(int i = 0; i < dirs.length; i++)
		{
			if(dircount == 10)
			{
				addToDB(keywords, keywordbinding);
				keywords.clear();
				dircount = 0;
			}
			if(dirs[i].isDirectory())
			{
				keywords = handleDirectory(dirs[i].getPath(), keywords, keywordbinding);
				dircounter++;
				System.out.println(dirs[i].getName() + " ---- " + dircounter);
				dircount++;
			}
			else if(dirs[i].isFile())
			{
				keywords = handleFile(dirs[i].getPath(), keywords);
			}
		}
		return keywords;
	}
	private ArrayList<KeyWords> handleFile(String filePath, ArrayList<KeyWords> keywords)
	{
		File f = new File(filePath);
		String fileName = f.getName();
		ArrayList<String> filewords = null;

		if(fileName.charAt(0) == '#'){
			return keywords;
		}
		String temp = "-1";
		int fName = -1;
		try{
			for(int k = 0; k < fileName.length(); k++){
				if(fileName.charAt(k) >= '0' && fileName.charAt(k) <= '9'){
					temp = fileName.substring(k, fileName.length() - 4);
					break;
				}

			}

			fName = Integer.parseInt(temp);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
		//get words in file

		try {
			//Tokenize the file 
			filewords = Tokenize2(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Remove stop words from the file 



		for(int k = 0; k < filewords.size(); k++)
		{
			int insertAt = Collections.binarySearch(keywords, filewords.get(k));
			if(insertAt < 0)
			{
				KeyWords kw = new KeyWords();
				kw.setWord(filewords.get(k));
				kw.addFileName(fName);
				insertAt = (-insertAt) -1;
				keywords.add(insertAt, kw);
			}
			else 
			{
				keywords.get(insertAt).addFileName(fName);
			}
		}
		return keywords;
	}
	private void addToDB(ArrayList<KeyWords> keywords, EntryBinding keywordbinding)
	{
		// Open the cursor. 
		Cursor cursor = null;
		try {
			cursor = myDbs.getInvertedDB().openCursor(null, null);
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i = 0; i < keywords.size(); i++)
		{
			String keyword = keywords.get(i).getWord();
			KeyWords kw = new KeyWords();

			try {
				OperationStatus os;
				// Cursors need a pair of DatabaseEntry objects to operate. These hold
				// the key and data found at any given position in the database.
				DatabaseEntry foundKey = new DatabaseEntry(keyword.getBytes("UTF-8"));
				DatabaseEntry foundData = new DatabaseEntry();

				os = cursor.getSearchKey(foundKey, foundData, LockMode.DEFAULT);

				if(os == OperationStatus.SUCCESS)
				{
					//merge the existing files for the keyword with the files found
					KeyWords dbkw = (KeyWords) keywordbinding.entryToObject(foundData);
					dbkw.MergeInsert(keywords.get(i).getFiles());					
					kw = dbkw;
				}
				else if(os == OperationStatus.NOTFOUND)
				{
					//keyword not added yet just insert file found
					kw = keywords.get(i);
				}

			} catch (DatabaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				theKey = new DatabaseEntry(keyword.getBytes("UTF-8"));
			} catch (IOException willNeverOccur) {}

			keywordbinding.objectToEntry(kw, theData);

			try {
				myDbs.getInvertedDB().put(null, theKey, theData);				
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static boolean binaryStringSearch(String[] stopwords, String word)
	{
		int left, right, mid = 0;

		left = 0;
		right = stopwords.length-1;

		while(left <= right)
		{
			mid = ((right-left)/2) + left;
			String cur = stopwords[mid];
			int result = word.compareToIgnoreCase(cur);
			if(result == 0)
			{
				return true; 	//found word
			}
			if(result > 0)
				left = mid+1;
			else
				right = mid-1;
		}
		return false;


	}

	public static ArrayList<String> Tokenize2(File filePath) throws IOException{

		String fileLine;
		ArrayList<String> keywords = new ArrayList<String>();
		Scanner scan = new Scanner(filePath, "UTF-8").useDelimiter("\\Z");
		fileLine = scan.next();

		for(int i = 0; i < fileLine.length(); i++){
			char x = fileLine.charAt(i);
			if(x == '<'){
				while(i < fileLine.length() && fileLine.charAt(++i)!= '>'){
					;
				}

			}else if(i < fileLine.length() && !delimiter.contains("" + fileLine.charAt(i))){
				String temp = "";
				while(i < fileLine.length() && !delimiter.contains("" + fileLine.charAt(i)))
				{
					temp += fileLine.charAt(i);
					i++;
				}

				if(!binaryStringSearch(stopwords, temp))

				{

					keywords.add(temp);
				}

			}

		}
		scan.close();
		return keywords;
	}


	protected DatabaseLoad() {}
}
