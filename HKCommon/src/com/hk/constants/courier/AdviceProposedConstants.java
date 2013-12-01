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
