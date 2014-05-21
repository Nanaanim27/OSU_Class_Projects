import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseException;


public class Assignment2 {
	
    private static String myDbsPath = "./";
    private static Mydbs mydbs;
	public static void main(String args[])
	{
		
		int taskswitch = 0;
		mydbs = new Mydbs();
		DatabaseInteractions dbi = new DatabaseInteractions(mydbs);
		
		if(args.length >= 1)
		{
			taskswitch = Integer.parseInt(args[0]);
		}
		
		switch(taskswitch)
		{
		case 1:
			dbi.InsertFiles();
			break;
		case 2:
			String fileName = args[1];
			int name = Integer.parseInt(fileName.substring(0, fileName.length()-4));
			dbi.GetFileName(name);
		    break;
		case 3:
			String fileName1 = args[1];
			int name1 = Integer.parseInt(fileName1.substring(0, fileName1.length()-4));
			String fileName2 = args[2];
			int name2 = Integer.parseInt(fileName2.substring(0, fileName2.length()-4));
			dbi.GetNameRange(name1, name2);
			break;
		case 4:
			long filesize = Long.parseLong(args[1]);
			dbi.GetFileSize(filesize);
			break;
		case 5:
			long filesize1 = Long.parseLong(args[1]);
			long filesize2 = Long.parseLong(args[2]);
			dbi.GetFileSizes(filesize1, filesize2);
			break;
		case 6:
			String fileNamea = args[1];
			int namea = Integer.parseInt(fileNamea.substring(0, fileNamea.length()-4));
			String fileNameb = args[2];
			int nameb = Integer.parseInt(fileNameb.substring(0, fileNameb.length()-4));
			long filesizea = Long.parseLong(args[3]);
			long filesizeb = Long.parseLong(args[4]);
			
			try {
				dbi.GetFileNamesWithSizes(namea, nameb, filesizea, filesizeb);
			} catch (DatabaseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			break;
		case 7:
			String content = args[1];
			dbi.GetFileWithContent(content);
			break;
		default:
			break;
				
		
		}
		try {
			mydbs.getStore().close();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
