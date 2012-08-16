package com.hk.admin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 14, 2012
 * Time: 3:15:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class XslUtil {

	private static Logger logger = LoggerFactory.getLogger(XslUtil.class);

	public static Date getDate(String value) {
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
			date = (Date) formatter.parse(value);
		} catch (ParseException pe) {
			logger.error("trying to format :" + value + " to format yyyy/MM");
		}
		return date;
	}

	public static Double getDouble(String value) {
		Double valueInDouble = null;
		try {
			valueInDouble = Double.parseDouble(value);
		} catch (NumberFormatException nfe) {
			logger.error("cannot parse to double::" + value);
		}
		return valueInDouble;
	}

	public static Long getLong(String value) {
		Long valueInLong = null;
		try {
			valueInLong = Long.parseLong(value.replace(".0", ""));
		} catch (NumberFormatException nfe) {
			logger.error("cannot parse to long::" + value);
		}
		return valueInLong;
	}

}
