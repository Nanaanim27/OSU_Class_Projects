import java.util.ArrayList;

import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseException;


public class Assignment3 {
	
    private static String myDbsPath = "./";
    private static MyDbs obj = new MyDbs();

	
	public static void main(String args[])
	{
		int taskswitch = 0;
		
		if(args.length >= 1)
		{
			taskswitch = Integer.parseInt(args[0]);
		}
		
		switch(taskswitch)
		{
		case 1:
			DatabaseLoad edl = new DatabaseLoad();
			edl.run(myDbsPath, args[1]);
			break;
		case 2:
			String[] keywords = new String[args.length-1];
			for(int i = 1; i < args.length; i++){
				keywords[i-1] = args[i];
			}
			ArrayList<Integer> l = null;
			DatabaseRead ed2 = new DatabaseRead();
			try {
				
				l = ed2.getMatches(keywords, myDbsPath);
				for(int j = 0; j < l.size(); j++)
				{
					System.out.println(l.get(j));
				}
				System.out.println(l.size());
			} catch (DatabaseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//DatabaseLoad ed2 = new DatabaseLoad();
			
		    //ed2.retrieve(myDbsPath, fileName);
		    
			break;
		default:
			break;
				
		
		}
		
	}
}
