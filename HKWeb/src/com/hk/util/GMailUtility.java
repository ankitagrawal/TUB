package com.hk.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMailUtility {

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final int SMTP_HOST_PORT = 465;
	private static final String orderEmailSubject = "New Order";
	private static final String gmailUserName = "healthchakra";
	private static final String gmailPassword = "admin2K9!";

	public static void main(String[] args) throws Exception {
		new GMailUtility().sendOrderMail("order@healthchakra.com", "Test");
	}

	public void sendOrderMail(String recipient, String emailContent)
			throws MessagingException {
		Properties props = new Properties();

		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", SMTP_HOST_NAME);
		props.put("mail.smtps.auth", "true");
		props.put("mail.smtps.quitwait", "false");

		Session mailSession = Session.getDefaultInstance(props);
		mailSession.setDebug(true);
		Transport transport = mailSession.getTransport();

		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject(orderEmailSubject);
		message.setContent(emailContent, "text/plain");

		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				recipient));

		transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, gmailUserName,
				gmailPassword);

		transport.sendMessage(message, message
				.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

	public void sendEnquiryMail(String recipient, String emailContent)
			throws MessagingException {
		Properties props = new Properties();

		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtps.host", SMTP_HOST_NAME);
		props.put("mail.smtps.auth", "true");
		props.put("mail.smtps.quitwait", "false");

		Session mailSession = Session.getDefaultInstance(props);
		mailSession.setDebug(true);
		Transport transport = mailSession.getTransport();

		MimeMessage message = new MimeMessage(mailSession);
		message.setSubject("New Enquiry");
		message.setContent(emailContent, "text/plain");

		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				recipient));

		transport.connect(SMTP_HOST_NAME, SMTP_HOST_PORT, gmailUserName,
				gmailPassword);

		transport.sendMessage(message, message
				.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

}
