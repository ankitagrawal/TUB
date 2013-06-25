package com.hk.constants.inventory;

import com.hk.domain.inventory.rv.ReconciliationType;

import java.util.Arrays;
import java.util.List;


/**
 * Generated
 */
public enum EnumReconciliationType {
    Add(10L, "Add"),
    Subtract(20L, "Subtract"),
    SubtractDamage(30L, "Damage-Subtract"),
    SubtractExpired(40L, "Expired-Subtract"),
    Lost(50L, "Lost-Subtract"),
    SubtractBatchMismatch(60L, "Batch Mismatch-Subtract"),
    MrpMismatch(70L, "Mrp Mismatch-Subtract"),
    NonMoving(80L, "Non Moving-Subtract"),
    SubtractFreeVariant(90L, "Free Variant-Subtract"),
    CustomerReturn(100L, "Customer Return-Add"),
    PharmaReturn(110L, "Pharma Return-Add"),
    ProductVariantAudited(120L, "Product Variant Audited-Subtract"),
    SubtractIncorrectCounting(130L, "Incorrect Counting-Subtract"),
    AddIncorrectCounting(140L, "Incorrect Counting-Add"),
    AddDamage(150L, "Damage-Add"),
    AddExpired(160L, "Expired-Add"),
    AddBatchMismatch(170L, "Batch Mismatch-Add"),
    AddFreeVariant(180L, "Free Variant-Add"),
    RVForDebitNote(190L, "RV For Debit Note");


    private java.lang.String name;
    private java.lang.Long id;

    EnumReconciliationType(java.lang.Long id, java.lang.String name) {
        this.id = id;
        this.name = name;
    }

    public java.lang.Long getId() {
        return id;
    }

    public java.lang.String getName() {
        return name;
    }


    public static EnumReconciliationType getEnumReconciliationTypeById(Long id) {
        for (EnumReconciliationType enumReconciliationType : EnumReconciliationType.values()) {
            if (enumReconciliationType.getId().equals(id)) {
                return enumReconciliationType;
            }

        }
        return null;
    }

    public ReconciliationType asReconciliationType() {
        ReconciliationType reconciliationType = new ReconciliationType();
        reconciliationType.setId(id);
        reconciliationType.setName(name);
        return reconciliationType;
    }


    public static List<ReconciliationType> getSubtractReconciliationType() {
        return Arrays.asList(SubtractDamage.asReconciliationType(), SubtractExpired.asReconciliationType(), Lost.asReconciliationType(),
                SubtractBatchMismatch.asReconciliationType(), MrpMismatch.asReconciliationType(), NonMoving.asReconciliationType(), SubtractFreeVariant.asReconciliationType(), SubtractIncorrectCounting.asReconciliationType());
    }


    public static List<ReconciliationType> getAddReconciliationType() {
        return Arrays.asList(AddDamage.asReconciliationType(), AddExpired.asReconciliationType(), AddBatchMismatch.asReconciliationType(), CustomerReturn.asReconciliationType(),
                PharmaReturn.asReconciliationType(), AddFreeVariant.asReconciliationType(), AddIncorrectCounting.asReconciliationType());
    }

    public static ReconciliationType getSubtractReconciliationTypeByName(String reconReason) {
        List<EnumReconciliationType> reconciliationTypeList = Arrays.asList(Subtract, SubtractDamage, SubtractExpired, Lost,
                SubtractBatchMismatch, MrpMismatch, NonMoving, SubtractFreeVariant, SubtractIncorrectCounting);
        for (EnumReconciliationType enumReconciliationType : reconciliationTypeList) {
            if (enumReconciliationType.getName().equalsIgnoreCase(reconReason.trim())) {
                return enumReconciliationType.asReconciliationType();
            }
        }
        return null;
    }

    public static ReconciliationType getAddReconciliationTypeByName(String reconReason) {
        List<EnumReconciliationType> reconciliationTypeList = Arrays.asList(AddDamage, AddExpired, AddBatchMismatch, CustomerReturn, PharmaReturn, AddFreeVariant, AddIncorrectCounting);
        for (EnumReconciliationType enumReconciliationType : reconciliationTypeList) {
            if (enumReconciliationType.getName().equalsIgnoreCase(reconReason.trim())) {
                return enumReconciliationType.asReconciliationType();
            }
        }
        return null;
    }
    
    public static List<ReconciliationType> getDebitNoteReconciliationType() {
        return Arrays.asList(SubtractDamage.asReconciliationType(), SubtractExpired.asReconciliationType(), NonMoving.asReconciliationType());
    }

}

