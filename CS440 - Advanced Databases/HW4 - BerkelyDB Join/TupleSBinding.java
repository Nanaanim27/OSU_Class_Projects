import java.io.FileWriter;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;


public class TupleSBinding extends TupleBinding {

    // Implement this abstract method. Used to convert
    // a DatabaseEntry to an Inventory object.
    public Object entryToObject(TupleInput ti) {

        int x = Integer.parseInt(ti.readString());
        int y = Integer.parseInt(ti.readString());
        TupleS row = new TupleS();
        row.setX(x);
        row.setY(y);

        return row;
    }

    // Implement this abstract method. Used to convert a
    // Inventory object to a DatabaseEntry object.
    public void objectToEntry(Object object, TupleOutput to) {

        TupleS row = (TupleS)object;
        
        to.writeString(""+row.getX());
        to.writeString(""+row.getY());
        //to.writeFast(row.getX());
        //to.writeFast(row.getY());
    }
    

}
