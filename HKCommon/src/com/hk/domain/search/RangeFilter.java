package com.hk.domain.search;

public class RangeFilter{
    private String name;
    private double startRange;
    private double endRange;

    public RangeFilter(String name, double startRange, double  endRange){
        this.name = name;
        this.startRange = startRange;
        this.endRange = endRange;
    }

    public double getStartRange() {
        return startRange;
    }

    public void setStartRange(double startRange) {
        this.startRange = startRange;
    }

    public double getEndRange() {
        return endRange;
    }

    public void setEndRange(double endRange) {
        this.endRange = endRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
