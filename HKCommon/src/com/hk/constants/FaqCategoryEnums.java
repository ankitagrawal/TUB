package com.hk.constants;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 12, 2012
 * Time: 12:57:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class FaqCategoryEnums {
    public static enum EnumFaqPrimaryCateogry {
        WeightManagement(1L, "Weight Management"),
        SportAndFitness(2L, "Sports and Fitness"),
        Nutrition(3L, "Nutrition");

        private String name;
        private Long id;

        EnumFaqPrimaryCateogry(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Long getId() {
            return id;
        }
    }

    public static enum EnumFaqSecondaryCateogry {

        WeightGain(1L, "Weight Gain"),
        WeightLoss(2L, "Weight Loss"),
        DietSupplements(3L, "Diet Supplements");

        private String name;
        private Long id;

        EnumFaqSecondaryCateogry(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Long getId() {
            return id;
        }
    }

    public static enum EnumPrimaryCategoryHasSecondaryCategory{
        WeightManagement(EnumFaqPrimaryCateogry.WeightManagement,
                Arrays.asList(
                        EnumFaqSecondaryCateogry.WeightGain,
                        EnumFaqSecondaryCateogry.WeightLoss
                )
        ),
        Nutrition(EnumFaqPrimaryCateogry.Nutrition,
                Arrays.asList(
                        EnumFaqSecondaryCateogry.DietSupplements
                )
        )
        ;

        EnumFaqPrimaryCateogry enumFaqPrimaryCateogry;
        List<EnumFaqSecondaryCateogry> enumFaqSecondaryCateogries;
        EnumPrimaryCategoryHasSecondaryCategory(EnumFaqPrimaryCateogry enumFaqPrimaryCateogry, List<EnumFaqSecondaryCateogry> enumFaqSecondaryCateogries){
            this.enumFaqPrimaryCateogry = enumFaqPrimaryCateogry;
            this.enumFaqSecondaryCateogries = enumFaqSecondaryCateogries;
        }

        public EnumFaqPrimaryCateogry getEnumFaqPrimaryCateogry() {
            return enumFaqPrimaryCateogry;
        }

        public List<EnumFaqSecondaryCateogry> getEnumFaqSecondaryCateogries() {
            return enumFaqSecondaryCateogries;
        }
    }

}
