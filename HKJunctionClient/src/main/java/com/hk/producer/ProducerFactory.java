package com.hk.producer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.hk.hkjunction.observers.OrderObserver;
import com.hk.hkjunction.observers.OrderObserver.OrderStatusMessage;

@Service
public class ProducerFactory {
  
   final int MAX_CONCURRENT_CONNECTIONS = 5;
   
   private JmsTemplate jmsTemplate;
   DefaultMessageListenerContainer messageContainer;
   ConnectionFactory activeMQConnectionFactory;

   OrderObserver orderObserver;
   @Autowired
   BeanFactory beanFactory;
  
   public ProducerFactory(String ipAddress){
	   
	   String connString = String.format("tcp://%s:61616?jms.prefetchPolicy.all=1",ipAddress);
	   PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(connString);
	   pooledConnectionFactory.setMaxConnections(MAX_CONCURRENT_CONNECTIONS);
	   activeMQConnectionFactory = pooledConnectionFactory.getConnectionFactory();
       jmsTemplate = new JmsTemplate(activeMQConnectionFactory);
       //For now we just have persistent messages. Though it hurts performance
       jmsTemplate.setDeliveryPersistent(true);      
       jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
   }
   
   @PostConstruct
   void init() throws JMSException{
	   
	   // Create a Connection
       Connection connection = activeMQConnectionFactory.createConnection();
       connection.start();
       // Create a Session
       Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
       // Create the destination (Topic or Queue)
       Destination destination = session.createQueue("order.cod.response");
       // Create a MessageConsumer from the Session to the Topic or Queue
       MessageConsumer consumer = session.createConsumer(destination);
       orderObserver = new OrderObserver();
       orderObserver.setBeanFactory(beanFactory);
       
//	   Queue queue = new ActiveMQQueue("order.cod.response");
//	   messageContainer = new DefaultMessageListenerContainer();
//       messageContainer.setConnectionFactory(activeMQConnectionFactory);
//       messageContainer.setDestination(queue);
//       
//       //orderObserver.setBeanFactory(beanFactory);
//       messageContainer.setMessageListener(orderObserver);
//       messageContainer.start();
       
       consumer.setMessageListener(orderObserver);
   }
   
 
   public void postMessage(String queueName,final String message){
	  Destination destination = null;	  
	  this.jmsTemplate.send(queueName, new MessageCreator() {
          public Message createMessage(Session session) throws JMSException {
            return session.createTextMessage(message);
          }
      });  
   }
   
   public void postCODMessage(final OrderStatusMessage message){
	   postMessage("order.cod", new Gson().toJson(message));
   }
   
   public void postPaymentFailureMessage(final String message){
	   postMessage("order.payment.failure", message);
   }
}
