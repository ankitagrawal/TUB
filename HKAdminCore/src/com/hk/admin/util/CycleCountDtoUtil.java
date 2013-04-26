package com.hk.admin.util;

import com.hk.admin.dto.inventory.CycleCountDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 4/5/13
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CycleCountDtoUtil {

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
            if (cycleCountDto.getProduct() != null) {
                productIdList.add(cycleCountDto.getProduct().getId());
            }

        }
        return productIdList;

    }

    //create List of brands name in DTO
    public static List<String> getCycleCountInProgressForVariant(List<CycleCountDto> cycleCountDtoList) {
        List<String> variantIdList = new ArrayList<String>();
        for (CycleCountDto cycleCountDto : cycleCountDtoList) {
            if (cycleCountDto.getProductVariant() != null) {
                variantIdList.add(cycleCountDto.getProductVariant().getId());
            }

        }
        return variantIdList;

    }

    public static String getCycleCountInProgress(List<CycleCountDto> cycleCountInProgressForVariantList) {
        StringBuilder auditNeedToBeClosed = new StringBuilder("Cycle Count In Progress For :: ").append("<br/>");
        for (CycleCountDto cycleCountDto : cycleCountInProgressForVariantList) {
            if (cycleCountDto.getBrand() != null) {
                auditNeedToBeClosed.append(cycleCountDto.getBrand()).append("<br/>");
            } else if (cycleCountDto.getProduct() != null) {
                auditNeedToBeClosed.append(cycleCountDto.getProduct().getId()).append("<br/>");
            } else if (cycleCountDto.getProductVariant() != null) {
                auditNeedToBeClosed.append(cycleCountDto.getProductVariant().getId()).append("<br/>");
            }

        }

        return auditNeedToBeClosed.toString();
    }


}
