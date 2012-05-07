package com.hk.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SMSUtility {

	public static void main(String[] args) {
		(new SMSUtility()).sendSMS("9560677555", "Test 1234567890");
	}

	public String sendSMS(String phone, String message) {
		boolean debug = true;
		String ppgHost = "http://bulksms.mysmsmantra.com:8080/WebSMS/SMSAPI.jsp";
		String username = "healthchakr";
		String password = "1664566628";
		String sender = "HLTHKART";
		String res = "";
		try {
			phone = URLEncoder.encode(phone, "UTF-8");
			if (debug)
				System.out.println("phone------>" + phone);
			message = URLEncoder.encode(message, "UTF-8");
			if (debug) {
				System.out.println("message---->" + message);
				System.out.println("message length---->" + message.length());
				if (message.length() > 160)
					message = "Welcome to HealthKart";
			}
			sender = URLEncoder.encode(sender, "UTF-8");
			if (debug)
				System.out.println("sender---->" + sender);

		} catch (UnsupportedEncodingException e) {
			System.out.println("Encoding not supported");
		}
		String url_str = ppgHost + "?username=" + username + "&password="
				+ password + "&mobileno=" + phone + "&message=" + message
				+ "&sendername=" + sender;
		if (debug)
			System.out.println("url string->" + url_str);
		try {
			URL url2 = new URL(url_str);
			HttpURLConnection connection = (HttpURLConnection) url2
					.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setConnectTimeout(10000);
			if (debug)
				System.out.println("Opened Con->" + connection);

			connection.connect();

			int code = connection.getResponseCode();
			System.out.println("code ->" + code);
			if (code == HttpURLConnection.HTTP_OK) {
				res = "OK";
				System.out.println("Disconnecting Connection.");
				connection.disconnect();
			}
		} catch (IOException e) {
			System.out.println("Unable to connect url: " + e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
}
