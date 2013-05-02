package com.hk.constants.queue;

import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.queue.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
 * User: Pratham
 * Date: 15/04/13  Time: 16:22
*/
public enum EnumBucket {

    HOME_LIVING(1L,CategoryConstants.HOME_LIVING,EnumClassification.BASKET_CATEGORY_HOME_LIVING),
    NUTRITION(5L,CategoryConstants.NUTRITION,EnumClassification.BASKET_CATEGORY_NUTRITION),
    PARENTING(10L, CategoryConstants.BABY, EnumClassification.BASKET_CATEGORY_PARENTING),
    BEAUTY(20L, CategoryConstants.BEAUTY, EnumClassification.BASKET_CATEGORY_BEAUTY),
    DIABETES(30L, CategoryConstants.DIABETES, EnumClassification.BASKET_CATEGORY_DIABETES),
    EYE(40L, CategoryConstants.EYE, EnumClassification.BASKET_CATEGORY_EYE),
    HEALTH_DEVICES(50L, CategoryConstants.HEALTH_DEVICES, EnumClassification.BASKET_CATEGORY_HEALTH_DEVICES),
    PERSONAL_CARE(60L, CategoryConstants.PERSONAL_CARE, EnumClassification.BASKET_CATEGORY_PERSONAL_CARE),
    SERVICES(70L, CategoryConstants.SERVICES, EnumClassification.BASKET_CATEGORY_SERVICES),
    SPORTS(80L, CategoryConstants.SPORTS, EnumClassification.BASKET_CATEGORY_SPORTS),
    HEALTH_NUTRITION(90l, CategoryConstants.HEALTH_NUTRITION, EnumClassification.BASKET_CATEGORY_HEALTH_NUTRITION),
    SPORTS_NUTRITION(100L, CategoryConstants.SPORTS_NUTRITION, EnumClassification.BASKET_CATEGORY_SPORTS_NUTRITION),
    Cheque_Cash_Neft(110L, "Cheque_Cash_Neft", EnumClassification.Cheque_Cash_Neft),
    Online_Payment_Disputes(120L, "Online_Payment_Disputes", EnumClassification.Online_Payment_Disputes),
    Cod_Confirmation(210L, "Cod", EnumClassification.COD),
    Jit(220L, "Jit", EnumClassification.JIT),
    DropShip(230L, "DropShip", EnumClassification.DropShip),
    ServiceOrder(240L, "ServiceOrder", EnumClassification.Services),
    Dispatch_Issues(310L, "Dispatch_Issues", EnumClassification.Dispatch_Issues),
    Warehouse(410L, "Warehouse_Processing", EnumClassification.Warehouse),
    AD_HOC(460L, "AD_HOC Cases", EnumClassification.Warehouse),
    CM(470L,"CM",EnumClassification.AD_HOC),
    Vendor(510L, "Vendor", EnumClassification.Vendor);

    private static Logger logger        = LoggerFactory.getLogger(EnumBucket.class);


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
//        bucket.setName(name);
//        bucket.setClassification(enumClassification.asClassification());
        return bucket;
    }

    public static List<Long> getBucketIDs(List<EnumBucket> enumBuckets) {
        List<Long> shippingOrderStatusIds = new ArrayList<Long>();
        for (EnumBucket enumBucket : enumBuckets) {
            shippingOrderStatusIds.add(enumBucket.getId());
        }
        return shippingOrderStatusIds;
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
            logger.debug("categoryName " + bucketName);
            for (EnumBucket enumBucket : EnumBucket.values()) {
                logger.debug("bucketName " + enumBucket.getName());
                if (bucketName.equalsIgnoreCase(enumBucket.getName())) {
                    logger.debug("bucketName matches categoryName");
                    applicableBuckets.add(enumBucket);
                    break;
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
