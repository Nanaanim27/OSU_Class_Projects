import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;


public class DatabaseInteractions {
	
	private Mydbs mydbs;
	
	public DatabaseInteractions(Mydbs mydb){
		mydbs = mydb;
	}
	
	public void GetFileName(int name)
	{
		String filename = "./ScottJordan_NickPepperling_2.txt";
		File file =new File(filename);
		FileWriter fileWritter;
        BufferedWriter bufferWritter = null;
        
		PrimaryIndex p = mydbs.getFileNameIndex();
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			XmlFile xfile = (XmlFile) p.get(name);
			fileWritter = new FileWriter(file.getName(),true);
	        bufferWritter = new BufferedWriter(fileWritter); 
	        bufferWritter.write(String.format("%s.xml: %d", xfile.getFileName(), xfile.getFileSize()));
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void GetNameRange(int nameStart, int nameEnd)
	{
		String filename = "./ScottJordan_NickPepperling_3.txt";
		File file =new File(filename);
		FileWriter fileWritter;
        BufferedWriter bufferWritter = null;
		PrimaryIndex p = mydbs.getFileNameIndex();
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			EntityCursor ec;
			ec = p.entities(nameStart, true, nameEnd, true);
			XmlFile xfile = (XmlFile) ec.first();
			while(xfile != null)
			{
				fileWritter = new FileWriter(file.getName(),true);
    	        bufferWritter = new BufferedWriter(fileWritter); 
    	        bufferWritter.write(String.format("%s.xml: %d", xfile.getFileName(), xfile.getFileSize()));
				
				xfile = (XmlFile) ec.next();
			}
			
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void GetFileSize(long size)
	{
		String filename = "./ScottJordan_NickPepperling_4.txt";
		File file =new File(filename);
		FileWriter fileWritter;
        BufferedWriter bufferWritter = null;
		SecondaryIndex p = mydbs.getFileSizeIndex();
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			EntityCursor ec;
			ec = p.entities(size, true, size, true);
			XmlFile xfile = (XmlFile) ec.first();
			while(xfile != null)
			{
				fileWritter = new FileWriter(file.getName(),true);
    	        bufferWritter = new BufferedWriter(fileWritter); 
    	        bufferWritter.write(String.format("%s.xml: %d", xfile.getFileName(), xfile.getFileSize()));
				xfile = (XmlFile) ec.next();
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void GetFileSizes(long sizeStart, long sizeEnd)
	{
		String filename = "./ScottJordan_NickPepperling_5.txt";
		File file =new File(filename);
		FileWriter fileWritter;
        BufferedWriter bufferWritter = null;
		SecondaryIndex p = mydbs.getFileSizeIndex();
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			EntityCursor ec;
			ec = p.entities(sizeStart, true, sizeEnd, true);
			XmlFile xfile = (XmlFile) ec.first();
			while(xfile != null)
			{
				fileWritter = new FileWriter(file.getName(),true);
    	        bufferWritter = new BufferedWriter(fileWritter); 
    	        bufferWritter.write(String.format("%s.xml: %d", xfile.getFileName(), xfile.getFileSize()));
				xfile = (XmlFile) ec.next();
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void GetFileNamesWithSizes(int nameStart, int nameEnd, long sizeStart, long sizeEnd) throws DatabaseException { 
        
		String filename = "./ScottJordan_NickPepperling_6.txt";
		File file =new File(filename);
		FileWriter fileWritter;
        BufferedWriter bufferWritter = null;
		PrimaryIndex p = mydbs.getFileNameIndex();
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			EntityCursor pkc;
			pkc = p.entities(nameStart, true, nameEnd, true);

			XmlFile pfile = (XmlFile) pkc.first();
			while(pfile != null)
			{
				long filesize = pfile.getFileSize();
				
				if(filesize >= sizeStart && filesize <= sizeEnd)
				{
					fileWritter = new FileWriter(file.getName(),true);
	    	        bufferWritter = new BufferedWriter(fileWritter); 
	    	        bufferWritter.write(String.format("%s.xml: %d", pfile.getFileName(), pfile.getFileSize()));
				}
				pfile = (XmlFile) pkc.next();
			}
			
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void GetFileWithContent(String content)
	{
		String filename = "./ScottJordan_NickPepperling_7.txt";
		File file =new File(filename);
		FileWriter fileWritter;
        BufferedWriter bufferWritter = null;
		
		PrimaryIndex p = mydbs.getFileNameIndex();
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			EntityCursor c;
			c = p.entities();
			Pattern pattern = Pattern.compile(content);
		    
		    Matcher matcher;

			XmlFile pfile = (XmlFile) c.first();
			while(pfile != null)
			{
				matcher = pattern.matcher(pfile.getContent());
				
				if(matcher.find())
				{
					
					fileWritter = new FileWriter(file.getName(),true);
	    	        bufferWritter = new BufferedWriter(fileWritter); 
	    	        bufferWritter.write(String.format("%s.xml: %d", pfile.getFileName(), pfile.getFileSize()));						
				}
				pfile = (XmlFile) c.next();
			}
			bufferWritter.close();
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void InsertFiles()
	{
		PrimaryIndex p = mydbs.getFileNameIndex();
		String imdb = "/scratch/cs440/imdb/";
        File f = new File(imdb);
    	File dirs[] = f.listFiles();
    	for(int i=0; i < dirs.length; i++)
    	{
    		File files[] = dirs[i].listFiles();
    		for(int j=0; j < files.length; j++)
    		{
    			String fileName = files[j].getName();
    			long fileSize = files[j].length();
    			
    			String filestr = "";
				try {
					Scanner s = new Scanner(files[j]);
					filestr = s.useDelimiter("\\Z").next();
					s.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
    			

    			XmlFile theFile = new XmlFile();
    			int name = Integer.parseInt(fileName.substring(0, fileName.length()-4));
    			theFile.setFileName(name);
    			theFile.setFileSize(fileSize);
    			theFile.setContent(filestr);
    			
    			try {
					//System.out.println("Inserting file: " + theFile.getFileName());
    				p.put(theFile);
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	} 
    		System.out.println("Inserted All Files");
	}
	
}
