package com.hk.constants.reversePickup;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/23/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumWarehouseQAStatus {
    Genuine(10L, "Genuine"),
    Ingenuine(20L, "Ingenuine"),
    Not_Applicable(30L, "Not Applicable");

    private Long id;
    private String name;

    EnumWarehouseQAStatus(Long id, String actionTaken) {
        this.id = id;
        name = actionTaken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
