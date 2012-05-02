package com.hk.util.io;

import java.util.Map;

/**
 * @author vaibhav.adlakha
 */
public class HKRow {
  private int lineNo;
  private Map<Object, Integer> headerPositions;
  private Object[] columnValues;

  public HKRow(int lineNo, Object[] columnValues) {
    this.lineNo = lineNo;
    this.columnValues = columnValues;
  }

  public HKRow(int lineNo, Object[] columnValues, Map<Object, Integer> headerPositions) {
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
  public Object getColumnValue(int index) {
    if (columnValues != null) {
      if (index < 0 || index >= columnValues.length) {
        return null;
      }
      return columnValues[index];
    }
    return null;
  }

  public Object getColumnValue(String columnName) {
    if (headerPositions == null) {
      throw new UnsupportedOperationException("Access by Column Name is not allowed for files without header.");
    }
    Integer columnIndex = headerPositions.get(columnName);
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
