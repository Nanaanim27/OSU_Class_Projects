import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;


public class TupleWXYZBinding {
	// Implement this abstract method. Used to convert
    // a DatabaseEntry to an Inventory object.
    public Object entryToObject(TupleInput ti) {

        int x = ti.read();
        int y = ti.read();
        byte[] w = new byte[8000];
        byte[] z = new byte[8000];
        
        for(int i = 0; i < 8000; i++)
        {
        	w[i] = ti.readByte();
        }
        for(int i = 0; i < 8000; i++)
        {
        	z[i] = ti.readByte();
        }
        TupleWXYZ row = new TupleWXYZ();
        row.setX(x);
        row.setY(y);
        row.setW(w);
        row.setZ(z);
        
        return row;
    }

    // Implement this abstract method. Used to convert a
    // Inventory object to a DatabaseEntry object.
    public void objectToEntry(Object object, TupleOutput to) {

        TupleWXYZ row = (TupleWXYZ)object;
        
        to.writeFast(row.getX());
        to.writeFast(row.getY());
        to.writeFast(row.getW());
        to.writeFast(row.getZ());
    }
}
