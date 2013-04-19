package com.hk.store;

public class SearchCriteria {

	private String categoryName;
	private Range range; 
	private int startRow;
	private int maxRows;

	public int getStartRow() {
		return this.startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getMaxRows() {
		return this.maxRows;
	}

	public void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}
	
	public String getCategoryName() {
		return this.categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public Range getRange() {
		return this.range;
	}
	
	public void setRange(Range range) {
		this.range = range;
	}
	
	public static class Range {
		int start;
		int end;
		
		public Range(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}
}
