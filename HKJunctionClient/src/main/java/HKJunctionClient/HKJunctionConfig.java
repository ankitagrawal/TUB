package HKJunctionClient;


import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.Connection;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import com.hk.producer.Producer;
import com.hk.producer.ProducerFactory;


@Configuration
public class HKJunctionConfig {
	
//	@Bean(autowire = Autowire.BY_TYPE,name = "hkProducerFactory", destroyMethod = "destroy")
//	ProducerFactory producerFactory(){
//		ProducerFactory producerFactory = new ProducerFactory("localhost");
//		return producerFactory;		
//	}

//	@Bean(autowire = Autowire.BY_TYPE,name = "jmsFactory", destroyMethod = "destroy")
//    public org.apache.activemq.ActiveMQConnectionFactory  activeMQFactory(String ipAddress){
//        //PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
//        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
//        String connString = String.format("tcp://%s:61616",ipAddress);
//        activeMQConnectionFactory.setBrokerURL(connString);
//        //pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
//         
//        return activeMQConnectionFactory;
//    }
	
//	  @Bean(autowire = Autowire.BY_TYPE,name = "jmsFactory")
//	  Session getSession(){
//		   javax.jms.Connection connection;
//		   
//			try {
//				connection = pooledConnectionFactory.createConnection();
//			    connection.start();
//			    session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);			    
//			} catch (JMSException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	   }
}
