package com.hk.constants.queue;

import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.queue.Bucket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 16:22
*/
public enum EnumBucket {

    BEAUTY(1l, CategoryConstants.BEAUTY, EnumClassification.BASKET_CATEGORY_BEAUTY),
    PERSONAL_CARE(10l, CategoryConstants.PERSONAL_CARE, EnumClassification.BASKET_CATEGORY_BEAUTY),
    BABY(20l, CategoryConstants.BABY, EnumClassification.BASKET_CATEGORY_BEAUTY),
    DIABETES(30l, CategoryConstants.DIABETES, EnumClassification.BASKET_CATEGORY_BEAUTY),
    HEALTH_DEVICES(40l, CategoryConstants.HEALTH_DEVICES, EnumClassification.BASKET_CATEGORY_BEAUTY),
    SPORTS(50l, CategoryConstants.SPORTS, EnumClassification.BASKET_CATEGORY_BEAUTY),
    SPORTS_NUTRITION(60l, CategoryConstants.SPORTS_NUTRITION, EnumClassification.BASKET_CATEGORY_BEAUTY),
    HEALTH_NUTRITION(70l, CategoryConstants.HEALTH_NUTRITION, EnumClassification.BASKET_CATEGORY_BEAUTY),
    HOME_LIVING(80l, CategoryConstants.HOME_LIVING, EnumClassification.BASKET_CATEGORY_BEAUTY),
    EYE(90l, CategoryConstants.EYE, EnumClassification.BASKET_CATEGORY_BEAUTY),
    SERVICES(100l, CategoryConstants.SERVICES, EnumClassification.BASKET_CATEGORY_BEAUTY),
    Cheque_Cash_Neft(110l, "Cheque_Cash_Neft", EnumClassification.Cheque_Cash_Neft),
    Cod_Confirmation(120l, "Cod", EnumClassification.COD),
    Online_Payment_Disputes(130l, "Online_Payment_Disputes", EnumClassification.Online_Payment_Disputes),
    Jit(210l, "Cod", EnumClassification.JIT),
    DropShip(220l, "Cod", EnumClassification.DropShip),
    Services(230l, "Cod", EnumClassification.Services),
    Dispatch_Issues(310L, "Dispatch_Issues", EnumClassification.Dispatch_Issues),
    Warehouse(410L, "Warehouse_Processing", EnumClassification.Warehouse),
    AD_HOC(410L, "AD_HOC Cases", EnumClassification.Warehouse), Vendor(460L, "Vendor", EnumClassification.Vendor);

    private Long id;
    private String name;
    private EnumClassification enumClassification;

    EnumBucket(long id, String name, EnumClassification classification) {
        this.id = id;
        this.name = name;
        this.enumClassification = classification;
    }

    public Bucket asBucket(){
        Bucket bucket = new Bucket();
        bucket.setId(id);
        bucket.setName(name);
        bucket.setClassification(enumClassification.asClassification());
        return bucket;
    }

    public static List<Bucket> getBuckets(List<EnumBucket> enumBuckets){
        List<Bucket> buckets = new ArrayList<Bucket>();
        for (EnumBucket enumBucket : enumBuckets) {
           buckets.add(enumBucket.asBucket());
        }
        return buckets;
    }

    public static List<EnumBucket> findByName(Set<String> bucketNames) {
        List<EnumBucket> applicableBuckets = new ArrayList<EnumBucket>();
        for (String bucketName : bucketNames) {
            for (EnumBucket enumBucket : EnumBucket.values()) {
                if (bucketName.equals(enumBucket.name)) {
                    applicableBuckets.add(enumBucket);
                }
            }
        }
        return applicableBuckets;
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

    public EnumClassification getEnumClassification() {
        return enumClassification;
    }

    public void setEnumClassification(EnumClassification enumClassification) {
        this.enumClassification = enumClassification;
    }
}
