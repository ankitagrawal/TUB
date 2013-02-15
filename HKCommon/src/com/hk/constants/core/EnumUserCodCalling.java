package com.hk.constants.core;

import java.util.List;
import java.util.Arrays;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Feb 11, 2013
 * Time: 1:52:55 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumUserCodCalling {

	PENDING_WITH_THIRD_PARTY(10, "Waiting For Response"),
	CONFIRMED(20, "Sucessful"),
	CANCELLED(30,"Canceled"),
	OTHERS(40,"Other"),
	EMPTY_MSG_RECIEVED(50,"response comes with empty key press value");

	private int id;
	private String name;

	EnumUserCodCalling(int id, String name) {
		this.id = id;
		this.name = name;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
