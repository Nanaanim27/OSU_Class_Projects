import java.util.Comparator;


public class TupleS implements Comparable<TupleS>, Comparator<TupleS>{
	private int x;
	private int y;
	
	public TupleS()
	{
	}
	public void setX(int x)
	{
		this.x = x;
	}
	public void setY(int y)
	{
		this.y = y;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	
	public int compareTo(Integer arg) {
		if(this.x < arg) return -1; 
		else if(this.x > arg) return 1;
		else return 0;
	}
	public int compareTo(TupleS arg) {
		if(this.x < arg.x) return -1; 
		else if(this.x > arg.x) return 1;
		else return 0;
	}
	public int compareTo(TupleRU arg)
	{
		if(this.x < arg.getPrimaryKey()) return -1;
		else if(this.x > arg.getPrimaryKey()) return 1;
		else return 0;
	}
	public int compare(TupleS tp1, TupleS tp2) {
		return tp1.compareTo(tp2);
	}
	
}
