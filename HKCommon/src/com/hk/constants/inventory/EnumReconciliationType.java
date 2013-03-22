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
    Damage(30L, "Damage"),
    Expired(40L, "Expired"),
    Lost(50L, "Lost"),
    BatchMismatch(60L, "Batch Mismatch"),
    MrpMismatch(70L, "Mrp Mismatch"),
    NonMoving(80L, "Non Moving"),
    FreeVariant(90L, "Free Variant"),
    CustomerReturn(100L, "Customer Return"),
    PharmaReturn(110L, "Pharma Return"),
    ProductVariantAudited(120L,"Product Variant Audited");


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


    public ReconciliationType asReconciliationType() {
        ReconciliationType reconciliationType = new ReconciliationType();
        reconciliationType.setId(id);
        reconciliationType.setName(name);
        return reconciliationType;
    }


    public static List<ReconciliationType> getSubtractReconciliationType() {
        return Arrays.asList( Damage.asReconciliationType(), Expired.asReconciliationType(), Lost.asReconciliationType(),
                BatchMismatch.asReconciliationType(), MrpMismatch.asReconciliationType(), NonMoving.asReconciliationType(), FreeVariant.asReconciliationType());
    }


    public static List<ReconciliationType> getAddReconciliationType() {
        return Arrays.asList(Damage.asReconciliationType(), Expired.asReconciliationType(), BatchMismatch.asReconciliationType(), CustomerReturn.asReconciliationType(),
                PharmaReturn.asReconciliationType(), FreeVariant.asReconciliationType());
    }

    public static ReconciliationType getSubtractReconciliationTypeByName(String reconReason) {
        List<EnumReconciliationType> reconciliationTypeList = Arrays.asList(Subtract, Damage, Expired, Lost,
                BatchMismatch, MrpMismatch, NonMoving, FreeVariant);
        for (EnumReconciliationType enumReconciliationType : reconciliationTypeList) {
            if (enumReconciliationType.getName().equalsIgnoreCase(reconReason.trim())) {
                return enumReconciliationType.asReconciliationType();
            }
        }
        return null;
    }

    public static ReconciliationType getAddReconciliationTypeByName(String reconReason) {
        List<EnumReconciliationType> reconciliationTypeList = Arrays.asList(Damage, Expired, BatchMismatch, CustomerReturn, PharmaReturn, FreeVariant);
        for (EnumReconciliationType enumReconciliationType : reconciliationTypeList) {
            if (enumReconciliationType.getName().equalsIgnoreCase(reconReason.trim())) {
                return enumReconciliationType.asReconciliationType();
            }
        }
        return null;
    }

}

