package com.hk.api.client.dto;

import com.hk.api.client.constants.EnumHKAPIErrorCode;
import com.hk.api.client.constants.HKAPIOperationStatus;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 12/2/12
 * Time: 2:12 PM
 */
public class HKAPIBaseDto {
    private Object data;
    private String operationType;
    private String status = HKAPIOperationStatus.OK;
    private int errorCode;
    private String message;

    public HKAPIBaseDto() {
        super();
    }

    public HKAPIBaseDto(String status, int errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    public HKAPIBaseDto(EnumHKAPIErrorCode enumErrorCode){
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
