package com.hk.api.dto.order;

import com.hk.api.dto.user.HKAPIUserDTO;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 */
public class HKAPIOrderDTO {
    private HKAPIUserDTO hkapiUserDTO;
    private HKAPIOrderDetailsDTO hkapiOrderDetailsDTO;
    private HKAPIPaymentDTO hkapiPaymentDTO;
    private HKAPIAddressDTO hkapiAddressDTO;
    private Long storeId;

    public HKAPIUserDTO getHkapiUserDTO() {
        return hkapiUserDTO;
    }


    public void setHkapiUserDTO(HKAPIUserDTO hkapiUserDTO) {
        this.hkapiUserDTO = hkapiUserDTO;
    }

    public HKAPIOrderDetailsDTO getHkapiOrderDetailsDTO() {
        return hkapiOrderDetailsDTO;
    }


    public void setHkapiOrderDetailsDTO(HKAPIOrderDetailsDTO hkapiOrderDetailsDTO) {
        this.hkapiOrderDetailsDTO = hkapiOrderDetailsDTO;
    }


    public HKAPIPaymentDTO getHkapiPaymentDTO() {
        return hkapiPaymentDTO;
    }


    public void setHkapiPaymentDTO(HKAPIPaymentDTO hkapiPaymentDTO) {
        this.hkapiPaymentDTO = hkapiPaymentDTO;
    }


    public HKAPIAddressDTO getHkapiAddressDTO() {
        return hkapiAddressDTO;
    }


    public void setHkapiAddressDTO(HKAPIAddressDTO hkapiAddressDTO) {
        this.hkapiAddressDTO = hkapiAddressDTO;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
