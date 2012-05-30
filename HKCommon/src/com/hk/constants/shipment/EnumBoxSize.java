package com.hk.constants.shipment;

import com.hk.domain.courier.BoxSize;

public enum EnumBoxSize {

    XS(10L, "XS", 100D),
    S(20L, "S", 400D),
    M(30L, "M", 1000D),
    M2(35L, "M2", 1520D),
    L(40L, "L", 2300D),
    L2(45L, "L2", 3600D),
    XL(50L, "XL", 4300D),
    XXL(60L, "XXL", 8500D),
    XXXL(70L, "XXXL", 1050D),
    MIGRATE(-1L, "MIGRATE", 0D);

    private String name;
    private Long id;
    private Double weight;

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

    EnumBoxSize(Long id, String name, Double weight) {
        this.name = name;
        this.id = id;
        this.weight = weight;
    }

    public Double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

}