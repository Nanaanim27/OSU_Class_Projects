import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;


public class TupleInterBinding {
	// Implement this abstract method. Used to convert
    // a DatabaseEntry to an Inventory object.
    public Object entryToObject(ByteArrayInputStream istream, int off) {
    	byte[] w = new byte[8000];
    	istream.read(w, off, 8000);
    	
    	int x = 0;
    	x += (w[0] << 24);
    	x += (w[1] << 16);
    	x += (w[2] << 8);
    	x += (w[3] /*<< 0*/);
    	int y = 0;
    	y += (w[4] << 24);
    	y += (w[5] << 16);
    	y += (w[6] << 8);
    	y += (w[7] /*<< 0*/);
    	
        for(int i = 0; i < 8; i++)
        {
        	w[i] = 0;
        }
        
        TupleInter row = new TupleInter();
        row.setX(x);
        row.setY(y);
        row.setW(w);
        return row;
    }

    // Implement this abstract method. Used to convert a
    // Inventory object to a DatabaseEntry object.
    public void objectToEntry(Object object, ByteArrayOutputStream ostream, int off) {

    	TupleInter row = (TupleInter)object;
    	byte[] outBuf = row.getW();
    	int x = row.getX();
    	outBuf[0] = (byte) (x >> 24);
        outBuf[1] = (byte) (x >> 16);
        outBuf[2] = (byte) (x >> 8);
        outBuf[3] = (byte) (x /*>> 0*/);
        int y = row.getY();
        outBuf[4] = (byte) (y >> 24);
        outBuf[5] = (byte) (y >> 16);
        outBuf[6] = (byte) (y >> 8);
        outBuf[7] = (byte) (y /*>> 0*/);
        
        ostream.write(outBuf, off, 8000);
        
    }
}
