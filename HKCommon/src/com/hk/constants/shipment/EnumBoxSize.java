package com.hk.constants.shipment;

import com.hk.domain.courier.BoxSize;

public enum EnumBoxSize {

    XS(10L, "XS", 100D, 8.25D,30D),
    S(20L, "S", 400D, 12D,100D),
    M(30L, "M", 1000D, 15.5D,180D),
    M2(35L, "M2", 1520D, 17.6D,240D),
    L(40L, "L", 2300D, 17.7D,340D),
    L2(45L, "L2", 3600D, 22.5D,450D),
    XL(50L, "XL", 4300D, 31.9D,580D),
    XXL(60L, "XXL", 8500D, 37D,800D),
    XXXL(70L, "XXXL", 10500D, 57.2D,1200D),
    MIGRATE(-1L, "MIGRATE", 0D, 0D,0D);

    private String name;
    private Long id;
    private Double volumetricWeight;
    private Double boxWeight;
    private Double packagingCost;

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

    EnumBoxSize(Long id, String name, Double weight, Double packagingCost, Double boxWeight) {
        this.name = name;
        this.id = id;
        this.volumetricWeight = weight;
        this.packagingCost = packagingCost;
        this.boxWeight = boxWeight;
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
}