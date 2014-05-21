import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;


public class TupleRUBinding extends TupleBinding {

    // Implement this abstract method. Used to convert
    // a DatabaseEntry to an Inventory object.
    public Object entryToObject(TupleInput ti) {

        int pkey = ti.read();
        byte[] arr = new byte[8000];
        for(int i = 0; i < 8000; i++)
        {
        	arr[i] = ti.readByte();
        }
        TupleRU row = new TupleRU();
        row.setPrimaryKey(pkey);
        row.setEightKB(arr);

        return row;
    }

    // Implement this abstract method. Used to convert a
    // Inventory object to a DatabaseEntry object.
    public void objectToEntry(Object object, TupleOutput to) {

        TupleRU row = (TupleRU)object;
        
        to.writeFast(row.getPrimaryKey());
        to.writeFast(row.getEightKB());
    }

}
