import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2004, 2012 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$ 
 */

// File: Inventory.java



public class KeyWords implements Serializable, Comparable<String>{

    private String word;
    private ArrayList<Integer> files;

    public void addFileName(int name) {
    	sortedInsert(name);
    }
    public String getWord()
    {
    	return this.word;
    }
    public void setWord(String w) {
        this.word = w;
    }
    public void setFiles(ArrayList<Integer> files)
    {
    	this.files = files;
    }
    public ArrayList<Integer> getFiles() {
        return files;
    }
    public KeyWords()
    {
    	files = new ArrayList<Integer>();
    	word = "";
    }
   
    private boolean sortedInsert(int val)
    {
    	int index = Collections.binarySearch(this.files, val);
    	if(index < 0)
    	{
    		index = (-index) - 1;
    		files.add(index, val);
    		return true;
    	}
    	else return false;
    }
    
    public void MergeInsert(ArrayList<Integer> toMerge)
    {
    	ArrayList<Integer> merged = new ArrayList<Integer>();
    	
    	int i = 0;
    	int j = 0;
    	
    	while (i < files.size() && j < toMerge.size())
    	{
    		int stored = files.get(i);
    		int toStore = toMerge.get(j);
    		if(stored < toStore)
    		{
    			merged.add(stored);
    			i++;
    		}
    		else if (stored > toStore) 
    		{
    			merged.add(toStore);
    			j++;
    		}
    		else
    		{
    			merged.add(toStore);
    			i++;
    			j++;
    		}
    	}
    	while(j < toMerge.size())
    		merged.add(toMerge.get(j++));
    	while(i < files.size())
    		merged.add(files.get(i++));
    	this.files = merged;
    }
	
	public int compareTo(KeyWords kw) {
		return word.compareToIgnoreCase(kw.getWord());
	}
	
	public int compareTo(String w) {
		return word.compareToIgnoreCase(w);
	}

}

