import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sleepycat.db.Cursor;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseException;


public class Assignment4 {

	private static MyDbs myDbs = new MyDbs();
	
	public static void main(String[] args) {
		
		try {
			myDbs.setup("./");
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			System.out.println("Found you mofo");
			e.printStackTrace();
		}
		if(args[0].equals("i"))
		{
		
			ArrayList<Integer> RX = readFile(args[1]);
			ArrayList<Integer> SX = readFile(args[2]); 
			ArrayList<Integer> SY = readFile(args[3]);
			ArrayList<Integer> UY = readFile(args[4]);

			Insert ins = new Insert(myDbs);
			ins.insertS(SX, SY);
			ins.insertRU(RX, 'r');
			ins.insertRU(UY, 'u');
		}
		if(args[0].equals("j")) {
			Join j = new Join(myDbs);
			j.MergeSortOnS();
		}
	}
	
	public static ArrayList<Integer> readFile(String fileName){
		
		File file = new File(fileName);
		ArrayList<Integer> nums = new ArrayList<Integer>();
		
		try {
			FileReader r = new FileReader(file);
			BufferedReader br = new BufferedReader(r);
			String line = br.readLine();
			line = line.trim();
			
			while(line != null){
				nums.add(Integer.parseInt(line));
				line = br.readLine();
				if(line != null) line = line.trim();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nums;
		
	}

	
}
