package com.hk.admin.dto.inventory;



import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/3/13
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CycleCountDto {

    String brand;
    String productId;
    String productVariantId;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    //create List of brands name in DTO
    public static List<String> getCycleCountInProgressForBrand(List<CycleCountDto> cycleCountDtoList) {
        List<String> brandList = new ArrayList<String>();
        for (CycleCountDto cycleCountDto : cycleCountDtoList) {
            if (cycleCountDto.getBrand() != null) {
                brandList.add(cycleCountDto.getBrand());
            }

        }
        return brandList;

    }

    //create List of Product names in DTO
    public static List<String> getCycleCountInProgressForProduct(List<CycleCountDto> cycleCountDtoList) {
        List<String> productIdList = new ArrayList<String>();
        for (CycleCountDto cycleCountDto : cycleCountDtoList) {
            if (cycleCountDto.getProductId() != null) {
                productIdList.add(cycleCountDto.getProductId());
            }

        }
        return productIdList;

    }

    //create List of brands name in DTO
    public static List<String> getCycleCountInProgressForVariant(List<CycleCountDto> cycleCountDtoList) {
        List<String> variantIdList = new ArrayList<String>();
        for (CycleCountDto cycleCountDto : cycleCountDtoList) {
            if (cycleCountDto.getProductVariantId() != null) {
                variantIdList.add(cycleCountDto.getProductVariantId());
            }

        }
        return variantIdList;

    }

    public  static String getCycleCountNeedToClose(List<CycleCountDto> cycleCountInProgressForVariantList) {
        StringBuilder auditNeedToBeClosed = new StringBuilder("Cycle Count In Progress For :: ").append("<br/>");
        for (CycleCountDto cycleCountDto : cycleCountInProgressForVariantList) {
            if (cycleCountDto.getBrand() != null) {
                auditNeedToBeClosed.append(cycleCountDto.getBrand()).append("<br/>");
            } else if (cycleCountDto.getProductId() != null) {
                auditNeedToBeClosed.append(cycleCountDto.getProductId()).append("<br/>");
            } else if (cycleCountDto.getProductVariantId() != null) {
                auditNeedToBeClosed.append(cycleCountDto.getProductVariantId()).append("<br/>");
            }

        }

        return auditNeedToBeClosed.toString();
    }


}
