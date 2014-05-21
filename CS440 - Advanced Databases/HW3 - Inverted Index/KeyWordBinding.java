/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2004, 2012 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$ 
 */

// File: InventoryBinding.java



import java.util.ArrayList;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class KeyWordBinding extends TupleBinding {

    // Implement this abstract method. Used to convert
    // a DatabaseEntry to an Inventory object.
    public Object entryToObject(TupleInput ti) {

        String word = ti.readString();
        String[] names = ti.readString().split(",");
        ArrayList<Integer> files = new ArrayList<Integer>();
       
        for(int i = 0; i < names.length; i++)
        {
        	files.add(Integer.parseInt(names[i]));
        }
        
        KeyWords file = new KeyWords();
        file.setWord(word);
        file.setFiles(files);

        return file;
    }

    // Implement this abstract method. Used to convert a
    // Inventory object to a DatabaseEntry object.
    public void objectToEntry(Object object, TupleOutput to) {

        KeyWords file = (KeyWords)object;

        to.writeString(file.getWord());
        ArrayList<Integer> files = file.getFiles();
        String fs = "";
        
        for(int i = 0; i < files.size(); i++)
        {
        	fs += files.get(i) + ",";
        }
        to.writeString(fs);

    }
}
