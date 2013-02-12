package com.hk.hkjunction.observers;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Service;

public class OrderObserver implements MessageListener{

	public enum ResponseSource{
		Knowlarity,
		EffortOut;
	}
	
	public enum OrderType{
		COD,
		PAYMENT_FAILURE;
	}
	
	private 
	BeanFactory beanFactory;

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		if (message instanceof TextMessage) {
            try {               
                String messageText = ((TextMessage) message).getText();
                CODOrderObserver codObserver = beanFactory.getBean(CODOrderObserver.class);
                codObserver.onCODResponse(messageText);
            }
            catch (JMSException ex) {
                //throw new RuntimeException(ex);
            }
        } 
	}
	

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	
	public class OrderStatusMessage{
		String orderId;
		String name;
	    long phone;
	    String email; 
	    String orderLink;
	    double amount;
	    OrderType orderType;
		public String getOrderId() {
			return orderId;
		}
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public long getPhone() {
			return phone;
		}
		public void setPhone(long phone) {
			this.phone = phone;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getOrderLink() {
			return orderLink;
		}
		public void setOrderLink(String orderLink) {
			this.orderLink = orderLink;
		}
		public OrderType getOrderType() {
			return orderType;
		}
		public void setOrderType(OrderType orderType) {
			this.orderType = orderType;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
		
		
		
	}	
}
