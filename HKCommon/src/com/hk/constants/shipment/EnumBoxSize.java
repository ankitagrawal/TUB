package com.hk.constants.shipment;

import com.hk.domain.courier.BoxSize;

import java.util.Arrays;
import java.util.List;

public enum EnumBoxSize {

    XS(10L, "XS", 100D, 8.25D, 30D, 13D, 8D, 5D),
    S(20L, "S", 400D, 12D, 100D, 8D, 13D, 21D),
    M(30L, "M", 1000D, 15.5D, 180D, 13D, 16D, 23D),
    M2(35L, "M2", 1520D, 17.6D, 240D, 17D, 17D, 28D),
    L(40L, "L", 2300D, 17.7D, 340D, 20D, 20D, 28D),
    L2(45L, "L2", 3600D, 22.5D, 450D, 26D, 26D, 28D),
    XL(50L, "XL", 4300D, 31.9D, 580D, 16D, 30D, 46D),
    XXL(60L, "XXL", 8500D, 37D, 800D, 30D, 31D, 45D),
    XXXL(70L, "XXXL", 10500D, 57.2D, 1200D, 0D, 0D, 0D),
    MIGRATE(-1L, "MIGRATE", 0D, 0D, 0D, 0D, 0D, 0D);

    private String name;
    private Long id;
    private Double volumetricWeight;
    private Double boxWeight;
    private Double packagingCost;
    private Double length;
    private Double breadth;
    private Double height;


    public static EnumBoxSize getBoxSize(BoxSize boxSize) {
        if (boxSize != null) {
            for (EnumBoxSize enumBoxSize : values()) {
                if (enumBoxSize.getId().equals(boxSize.getId())) {
                    return enumBoxSize;
                }
            }
        }
        return null;
    }

    public BoxSize asBoxSize() {
        BoxSize boxSize = new BoxSize();
        boxSize.setId(this.id);
        boxSize.setName(this.name);
        return boxSize;
    }

    EnumBoxSize(Long id, String name, Double weight, Double packagingCost, Double boxWeight, Double length, Double breadth, Double height) {
        this.name = name;
        this.id = id;
        this.volumetricWeight = weight;
        this.packagingCost = packagingCost;
        this.boxWeight = boxWeight;
        this.length = length;
        this.breadth = breadth;
        this.height = height;
    }

    public static List<EnumBoxSize> getAllEnumBoxSize() {
        return Arrays.asList(EnumBoxSize.XS, EnumBoxSize.S, EnumBoxSize.M, EnumBoxSize.M2, EnumBoxSize.L, EnumBoxSize.L2, EnumBoxSize.XL, EnumBoxSize.XXL, EnumBoxSize.XXXL, EnumBoxSize.MIGRATE);
    }

    public static String getLengthBreadthHeight(Double weight) {
        String lengthBreadthHeightString = null;
        List<EnumBoxSize> enumBoxSizeList = Arrays.asList(EnumBoxSize.XS, EnumBoxSize.S, EnumBoxSize.M, EnumBoxSize.M2, EnumBoxSize.L, EnumBoxSize.L2, EnumBoxSize.XL, EnumBoxSize.XXL);
        for (EnumBoxSize boxSize : enumBoxSizeList) {
            if (weight < (boxSize.getVolumetricWeight() / 1000)) {
                lengthBreadthHeightString = boxSize.getLength() + "-" + boxSize.getBreadth() + "-" + boxSize.getHeight();
                return lengthBreadthHeightString;
            }

        }
        return lengthBreadthHeightString;
    }

    public Double getVolumetricWeight() {
        return volumetricWeight;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public Double getPackagingCost() {
        return packagingCost;
    }

    public Double getBoxWeight() {
        return boxWeight;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getBreadth() {
        return breadth;
    }

    public void setBreadth(Double breadth) {
        this.breadth = breadth;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
}