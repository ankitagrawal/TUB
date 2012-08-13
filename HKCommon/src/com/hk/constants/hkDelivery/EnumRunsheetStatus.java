package com.hk.constants.hkDelivery;

import com.hk.domain.hkDelivery.RunsheetStatus;

import java.util.Arrays;
import java.util.List;


public enum EnumRunsheetStatus {

    Open     (10L, "Open"),
    Close    (20L, "Closed"),
    OnHold   (30L, "On Hold");


    private Long id;
    private String status;


    EnumRunsheetStatus(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RunsheetStatus asRunsheetStatus() {
    RunsheetStatus runsheetStatus = new RunsheetStatus();
    runsheetStatus.setId(this.getId());
    runsheetStatus.setStatus(this.getStatus());
    return runsheetStatus;
  }
    
    public static List<RunsheetStatus> getAll() {
        return Arrays.asList(
                EnumRunsheetStatus.Open.asRunsheetStatus(),
                EnumRunsheetStatus.Close.asRunsheetStatus(),
                EnumRunsheetStatus.OnHold.asRunsheetStatus());
    }
}