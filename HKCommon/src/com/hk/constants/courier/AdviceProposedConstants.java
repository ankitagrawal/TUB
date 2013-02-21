package com.hk.constants.courier;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 15, 2013
 * Time: 5:53:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdviceProposedConstants {

	public static final String Refund_Customer = "Refund Customer";
	public static final String Replace_Items = "Replace Items";
	public static final String Ingenuine_Case = "Ingenuine Case";

	public static List<String> getAdviceList(){
		return Arrays.asList(Refund_Customer, Replace_Items, Ingenuine_Case);
	}
}
//	RC("RC", "Refund Customer"),
//	RI("RI", "Replace Items"),
//	IC("IC", "Ingenuine Case");
//
//	private String name;
//	private String id;
//
//	EnumAdviceProposed(String id, String name){
//		this.name = name;
//		this.id = id;
//	}
//
//	public static List<EnumAdviceProposed> getAdviceList(){
//		List<EnumAdviceProposed> adviceProposedList = new ArrayList<EnumAdviceProposed>();
//		adviceProposedList.add(EnumAdviceProposed.RC);
//		adviceProposedList.add(EnumAdviceProposed.RI);
//		adviceProposedList.add(EnumAdviceProposed.IC);
//		return adviceProposedList;
//	}
//
//
//	public static String getAdviceProposedByName(String name){
//	   for(EnumAdviceProposed enumActAdviceProposed : values()){
//		   if(enumActAdviceProposed.getName().equals(name)){
//			   return enumActAdviceProposed.getId();
//		   }
//	   }
//		return null;
//	}
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//}
