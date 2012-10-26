package com.hk.rest.mobile.service.model;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 20, 2012
 * Time: 6:44:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MErrors {
List<MError> errs = new ArrayList<MError>();

	public void addError(MError e) {
		errs.add(e);
	}

	public List<MError> getErrs() {
		return errs;
	}
	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(MError e : errs){
			builder.append(e.toString()+"\n");
		}
		return builder.toString();
	}
}
