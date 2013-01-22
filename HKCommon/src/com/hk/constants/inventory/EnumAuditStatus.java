package com.hk.constants.inventory;

import java.util.*;


public enum EnumAuditStatus {
    Pending(0L, "Pending"),
    Done(1L, "Done"),
    Invalid(99L, "Invalid"),
    Deleted(999L, "Deleted");

    private String name;
    private Long id;

    EnumAuditStatus(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public static List<EnumAuditStatus> getAllList() {
        return Arrays.asList(
                EnumAuditStatus.Pending,
                EnumAuditStatus.Done,
                EnumAuditStatus.Invalid,
                EnumAuditStatus.Deleted);

    }

    @SuppressWarnings("unchecked")
    public static List<EnumAuditStatus> getListById(Long id) {
        if (id.equals(EnumAuditStatus.Pending.getId())) {
            return Arrays.asList(
                    EnumAuditStatus.Done,
                    EnumAuditStatus.Invalid,
                    EnumAuditStatus.Deleted);
        } else if (id.equals(EnumAuditStatus.Invalid.getId())) {
            return Arrays.asList(
                    EnumAuditStatus.Done,
                    EnumAuditStatus.Deleted);
        } else if (id.equals(EnumAuditStatus.Deleted.getId())) {
            return Arrays.asList(
                    EnumAuditStatus.Done,
                    EnumAuditStatus.Invalid);
        } else if (id.equals(EnumAuditStatus.Done.getId())) {
            return new ArrayList<EnumAuditStatus>();
        }
        return Collections.EMPTY_LIST;
    }

}