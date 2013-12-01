package com.hk.util.io;

import java.util.Iterator;


public interface FileParser {
   /**
     * Parses the file and provides an Iterator over rows in the file
     *
     * @return Iterator<Row> iterate over the lines of the file
     */
    public Iterator<HKRow> parse();
}
