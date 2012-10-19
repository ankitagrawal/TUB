package com.hk.rest.mobile.service.model;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 20, 2012
 * Time: 6:45:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class MError {
    String msg;
	int errCode;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getErrCode() {
		return errCode;
	}

	public void setErrCode(int errCode) {
		this.errCode = errCode;
	}
	public String  toString(){
		return msg;
	}
}
