package com.hk.constants.courier;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 3/15/13
 * Time: 8:06 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumAwbChangeReason {
        DUMMY_AWB(10L, "Dummy_Awb"),
        B2B_ORDER(20L, "B2b_Order"),
        CHANGED_BY_COURIER(30L,"Changed_By_Courier"),
        OTHERS(90L,"Others");

        private String name;
        private Long id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    private EnumAwbChangeReason(Long id, String name) {
            this.name = name;
            this.id = id;
        }
    public static List<EnumAwbChangeReason> getAllAwbChangeReason() {
            return Arrays.asList(DUMMY_AWB, B2B_ORDER,CHANGED_BY_COURIER, OTHERS);
        }
}


