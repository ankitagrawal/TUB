package com.hk.util.io;

import java.util.Map;
import java.util.HashMap;

/**
 * @author vaibhav.adlakha
 */
public class HKRow {
    private int                  lineNo;
    private Map<String, Integer> headerPositions ;
    public String[]              columnValues;

    public HKRow(int lineNo, String[] columnValues) {
        this.lineNo = lineNo;
        this.columnValues = columnValues;
    }

    public HKRow(int lineNo, String[] columnValues, Map<String, Integer> headerPositions) {
        this(lineNo, columnValues);
        this.headerPositions = headerPositions;
    }

    public int getLineNo() {
        return lineNo;
    }

    /**
     * Index starts from 0
     * 
     * @param index
     * @return
     */
    public String getColumnValue(int index) {
        if (columnValues != null) {
            if (index < 0 || index >= columnValues.length) {
                return null;
            }
            return columnValues[index];
        }
        return null;
    }

    public String getColumnValue(String columnName) {

        if (headerPositions == null) {
            throw new UnsupportedOperationException("Access by Column Name is not allowed for files without header.");
        }

        columnName = columnName.trim();
        Integer columnIndex = 0;

        for (Map.Entry<String, Integer> entry : headerPositions.entrySet()) {

            if (columnName.trim().equals(entry.getKey().trim())) {
                columnIndex = entry.getValue();
            }

        }
        
        
        if (columnIndex == null) {
            return null;
        }
        return getColumnValue(columnIndex);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (Object s : columnValues) {
            builder.append('"').append(s).append('"').append(',');
        }
        return builder.deleteCharAt(builder.length() - 1).append(']').toString();
    }

}
