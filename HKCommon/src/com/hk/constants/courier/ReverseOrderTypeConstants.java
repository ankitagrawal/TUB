package com.hk.constants.courier;

import java.util.List;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Apr 10, 2013
 * Time: 1:22:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReverseOrderTypeConstants {

	public static final String Healthkart_Managed = "Healthkart Managed";
	public static final String Customer_Sent= "Customer Sent";

	public static List<String> getReverseOrderTypes(){
		return Arrays.asList(Healthkart_Managed, Customer_Sent);
	}
}
