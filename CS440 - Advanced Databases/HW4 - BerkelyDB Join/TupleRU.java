
public class TupleRU {
	private int primarykey;
	private byte[] eightkb;
	
	public TupleRU()
	{
		eightkb = new byte[8000];	
	}
	public void setPrimaryKey(int key)
	{
		primarykey = key;
	}
	public void setEightKB(byte[] arr)
	{
		eightkb = arr;
	}
	public byte[] getEightKB()
	{
		return eightkb;
	}
	public int getPrimaryKey()
	{
		return primarykey;
	}
	
}
