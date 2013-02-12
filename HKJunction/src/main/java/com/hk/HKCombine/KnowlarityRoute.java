package com.hk.HKCombine;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.google.gson.Gson;
import com.hk.HKCombine.Messages.OrderStatusMessage;

public class KnowlarityRoute extends RouteBuilder {
	  @Override
	    public void configure() throws Exception {

	    	final StringBuffer knowlarityUrl = new StringBuffer();
	    	final Gson gson = new Gson();
	    	String mainUrl = "http://www.smartivr.in/api/voice/quickCall";    	
	    	from("activemq:queue:order.cod")
	    	.process(new Processor(){
	    		public void process(Exchange exchange) {
	    	       
	    	        String message = exchange.getIn().getBody().toString();
	    	        
	    	        OrderStatusMessage orderMessage = gson.fromJson(message, OrderStatusMessage.class);
	    	        String phoneBook = Long.toString(orderMessage.getPhone()) + "," + orderMessage.getName() + "," 
	    	        + Double.toString(orderMessage.getAmount()) + ","+ orderMessage.getOrderId();
	    	        
	    	        String header = String.format("username=health_kart&password=123456&ivr_id=800059891" +
	    	        		"&format=xml&phone_book='%s'",phoneBook);
	    	        exchange.getIn().setHeader(Exchange.HTTP_QUERY, header);
	    	       
	    	    }
	    	})
	    	
	    	.to(mainUrl);
	    }

}
