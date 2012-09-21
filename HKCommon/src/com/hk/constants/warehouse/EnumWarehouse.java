package com.hk.constants.warehouse;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Aug 9, 2012
 * Time: 1:12:19 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumWarehouse {

	//todo ankit, please see if it is inline with the current warehouseService

    Gurgoan(1, "Gurgoan"),
    Mumbai(2, "Mumbai");

    private String name;
    private int id;

    EnumWarehouse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}
