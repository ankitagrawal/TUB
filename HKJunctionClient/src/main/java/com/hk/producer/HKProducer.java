package com.hk.producer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

public class HKProducer implements Producer{

	MessageProducer producer;
	
	@Override
	public boolean publishMessage(Object message) {
		boolean success = false;
		try {
			producer.send((Message) message);
			success = true;
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return success;
	}
	
	public MessageProducer getProducer() {
		return producer;
	}
	
	public void setProducer(MessageProducer producer) {
		this.producer = producer;
	}
	
}
