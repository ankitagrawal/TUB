package com.hk.api.dto;

import com.hk.api.constants.EnumHKAPIErrorCode;
import com.hk.api.constants.HKAPIOperationStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 11/23/12
 */
public class HKAPIBaseDTO {
    private Object data;
    private String operationType;
    private String status = HKAPIOperationStatus.OK;
    private int errorCode;
    private String message;

    public HKAPIBaseDTO() {
        super();
    }

    public HKAPIBaseDTO(String status, int errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    public HKAPIBaseDTO(EnumHKAPIErrorCode enumErrorCode){
        this.errorCode=enumErrorCode.getId();
        this.message=enumErrorCode.getMessage();
        this.status= HKAPIOperationStatus.ERROR;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
