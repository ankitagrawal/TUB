package com.hk.constants;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: Jul 12, 2012
 * Time: 12:57:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class FaqCategoryEnums {
    public static enum EnumFaqPrimaryCateogry {
        WeightManagement(1L, "weight management"),
        SportAndFitness(2L, "sport And fitness"),
        Nutrition(3L, "nutrition");

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

        public static List<String> getAll(){
            return Arrays.asList(EnumFaqPrimaryCateogry.Nutrition.getName(),
                    EnumFaqPrimaryCateogry.WeightManagement.getName(),
                    EnumFaqPrimaryCateogry.SportAndFitness.getName()
                    );
        }
    }

    public static enum EnumFaqSecondaryCateogry {

        WeightGain(1L, "weight gain"),
        WeightLoss(2L, "weight loss"),
        DietSupplements(3L, "diet supplements");

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

        public static List<String> getAll(){
            return Arrays.asList(EnumFaqSecondaryCateogry.WeightGain.getName(),
                    EnumFaqSecondaryCateogry.WeightLoss.getName(),
                    EnumFaqSecondaryCateogry.DietSupplements.getName());
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
