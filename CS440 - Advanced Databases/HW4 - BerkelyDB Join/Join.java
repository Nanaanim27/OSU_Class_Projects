import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.sleepycat.db.Cursor;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.OperationStatus;


public class Join {
	
	private MyDbs mydbs;
	private int M = 64000;		//number of tuples in s that can fit into main memory
	private int B = 32000;		//number of tuples that can fit into one block
	private boolean isNull = false;
	public Join(MyDbs mydbs)
	{
		this.mydbs = mydbs;
	}
	
	public void MergeSortOnS() {
		int n = 0;
		n = readAndSortS();
		String sortedfile = mergeS(n);
		System.out.println(sortedfile);
	}
	
	private int readAndSortS()
	{
		
		
		
		Cursor secCursor = null;
		
		TupleSBinding tsb = new TupleSBinding();
		ArrayList<TupleS> tuples = new ArrayList<TupleS>();
		boolean atEnd = false;
		int fileID = 0;
		
		try {
			secCursor = mydbs.getTableS().openCursor(null, null);
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			System.out.println("db exception: creating cursor on S table");
			e1.printStackTrace();
		}
		
		while(!atEnd) {
			
			for (int i = 0; i < M; i++) {
				try {

					DatabaseEntry searchKey1 = new DatabaseEntry();
					DatabaseEntry foundData = new DatabaseEntry();
					
					if (secCursor.getNext(searchKey1, foundData, null) == OperationStatus.SUCCESS) {

						TupleS theFile = (TupleS) tsb.entryToObject(foundData);
			
						tuples.add(theFile);
					}
					else {
						System.out.println("End of DB");
						atEnd = true;
						break;
					}

				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					System.out.println("Another Damn Error");
					e.printStackTrace();
				}
			}
			System.out.println("Sorting tuples: number of tuples: " + tuples.size());
			Collections.sort(tuples);
			File f = null;
			try {
				f = new File("S"+fileID+".tmp");
				System.out.println("Writing to file S" + fileID + ".tmp");
				if(f.exists())
				{
					f.delete();
				}
				f.createNewFile();
				writeSTuples(tuples, f);
				tuples.clear();
				fileID++;
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return fileID;
	}
	private String mergeS(int numFiles)
	{
		if(numFiles == 1)
		{
			return "S0.tmp";
		}
		for(int n = 0; n < numFiles - 1; n += 2) {
			File fileA = new File("S" + n + ".tmp");
			File fileB = new File("S" + (n+1) + ".tmp");
			File fileC = new File("S" + (numFiles++) + ".tmp");
			
			if(fileC.exists()) fileC.delete();
			
			FileReader ar;
			FileReader br;
			BufferedReader Abuff = null; 
			BufferedReader Bbuff = null; 

			
			try {
				ar = new FileReader(fileA);
				br = new FileReader(fileB);
				Abuff = new BufferedReader(ar);
				Bbuff = new BufferedReader(br);
				
				ArrayList<TupleS> A = new ArrayList<TupleS>();
				ArrayList<TupleS> B = new ArrayList<TupleS>();
				ArrayList<TupleS> C = new ArrayList<TupleS>();
				boolean afileEnd = false;
				boolean bfileEnd = false;
				
				while(isNull == false){
					if(A.isEmpty()) {
						System.out.println("loading from A: " + fileA.getName());
						A = loadS(Abuff);
						if(isNull) afileEnd = true;
					}
					if(B.isEmpty()) {						
						System.out.println("loading from B: " + fileB.getName());
						B = loadS(Bbuff);
						if(isNull) bfileEnd = true;
					}
					
					int i = 0;
					int j = 0;
					while(i < A.size() && j < B.size()) {
						TupleS a = A.get(i);
						TupleS b = B.get(j);
						int result = a.compareTo(b);
						if(result <= 0) {
							C.add(a);
							A.remove(i);
						}
						else {
							C.add(b);
							B.remove(j);
						}
						if(C.size() >= this.B){
							writeSTuples(C, fileC);
							System.out.println("Number rows C: " + C.size() + ", A: " + A.size() + ", B: " + B.size());
							C.clear();
						}						
					}
					if(i == A.size()){
						A.clear();
					} else  if (j == B.size()){
						B.clear();
					}
				}
				writeSTuples(C, fileC);
				System.out.println("Number rows C: " + C.size() + ", A: " + A.size() + ", B: " + B.size());
				if(afileEnd) {
					if(!A.isEmpty()) {
						System.out.println("Number rows C: " + C.size() + ", A: " + A.size() + ", B: " + B.size());
						writeSTuples(A, fileC);
						A.clear();
					}
					isNull = false;
					System.out.println("Number of tuples left in B: " + B.size() + ", A: " + A.size());
					while(isNull == false) {
						if(B.isEmpty()) {
							System.out.println("loading from B: " + fileB.getName());
							B = loadS(Bbuff);
						}
						System.out.println("Number rows C: " + C.size() + ", A: " + A.size() + ", B: " + B.size());
						writeSTuples(B, fileC);
					}
				}
				else {
					if(!B.isEmpty()) {
						System.out.println("Number rows C: " + C.size() + ", A: " + A.size() + ", B: " + B.size());
						writeSTuples(B, fileC);
						B.clear();
					}
					isNull = false;
					System.out.println("Number of tuples left in A: " + A.size() + ", B: " + B.size());
					while(isNull == false) {
						if(A.isEmpty()) {
							System.out.println("loading from A: " + fileA.getName());
							A = loadS(Abuff);
						}
						System.out.println("Number rows C: " + C.size() + ", A: " + A.size() + ", B: " + B.size());
						writeSTuples(A, fileC);
					}
				}
				
				Abuff.close();
				Bbuff.close();
				ar.close();
				br.close();
				fileA.delete();
				fileB.delete();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "S" + (numFiles-1) + ".tmp";
			
	}
	private void writeSTuples(ArrayList<TupleS> tuples, File f)
	{
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(f, true);
			bw = new BufferedWriter(fw);
			
			for(int i = 0; i < tuples.size(); i++) {
				int x = tuples.get(i).getX();
				int y = tuples.get(i).getY();
				bw.write(x + "," + y + "\n");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private ArrayList<TupleS> loadS(BufferedReader buff){
		
		ArrayList<TupleS> tuples = new ArrayList<TupleS>();
		
		for(int i = 0; i < B; i++){
			String[] arr = new String[2];
			String line  = "nothing right now";
			try {
				
				line = buff.readLine();

				if(line == null) {
					isNull = true;
					return tuples;
				}
				arr = line.split(",");
				
				int x = Integer.parseInt(arr[0]);
				int y = Integer.parseInt(arr[1]); 
			
				TupleS tupX = new TupleS();
			
				tupX.setX(x);
				tupX.setY(y);
				
				tuples.add(tupX);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				System.out.println("Can not get the intergers X: -"+arr[0]+"- Y:-"+arr[1]+"-");
				System.out.println("the line it split on: -" + line + "-" );
			}
			
		
		}
		
		return tuples;
	}
}
