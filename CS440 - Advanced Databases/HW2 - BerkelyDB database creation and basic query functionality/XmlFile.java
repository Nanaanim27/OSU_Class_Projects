import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/*-
 * See the file LICENSE for redistribution information.
 *
 * Copyright (c) 2004, 2012 Oracle and/or its affiliates.  All rights reserved.
 *
 * $Id$ 
 */

// File: Inventory.java


@Entity
public class XmlFile {

	@PrimaryKey
    private int fileName;
    @SecondaryKey(relate=Relationship.MANY_TO_ONE)
	private long fileSize;
    private String content;

    public void setFileName(int name) {
        this.fileName = name;
    }

    public void setFileSize(long size) {
        this.fileSize = size;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getContent() {
        return content;
    }

}

