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
    public static List<EnumAuditStatus> getPossibleStatuses(Long statusId) {
        if (statusId.equals(EnumAuditStatus.Pending.getId())) {
            return Arrays.asList(
                    EnumAuditStatus.Done,
                    EnumAuditStatus.Invalid,
                    EnumAuditStatus.Deleted);
        } else if (statusId.equals(EnumAuditStatus.Invalid.getId())) {
            return Arrays.asList(
                    EnumAuditStatus.Deleted);
        } else if (statusId.equals(EnumAuditStatus.Deleted.getId())) {
            return Collections.EMPTY_LIST;
        } else if (statusId.equals(EnumAuditStatus.Done.getId())) {
            return Arrays.asList(
                    EnumAuditStatus.Invalid,
                    EnumAuditStatus.Deleted);
        }
        return Collections.EMPTY_LIST;
    }

}