package com.hk.splitter.test;

import java.util.Collection;
import java.util.Properties;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.splitter.LineItemClassification;
import com.hk.splitter.LineItemClassification.UniqueWhCombination;
import com.hk.splitter.LineItemContainer;

public class SplitterTester {

	public static void main(String[] args) throws Exception {

		Properties properties = new Properties();
		properties.load(SplitterTester.class.getResourceAsStream("order.data"));

		long time = System.currentTimeMillis();
		LineItemContainer container = new LineItemContainer();

		Set<Object> keySet = properties.keySet();
		for (Object key : keySet) {
			CartLineItem cartLineItem = new CartLineItem();
			cartLineItem.setId(Long.valueOf(((String) key).substring(1)));

			String ws = properties.getProperty((String) key);
			String[] wss = StringUtils.commaDelimitedListToStringArray(ws);
			for (String w : wss) {
				Warehouse warehouse = new Warehouse();
				warehouse.setId(Long.valueOf(w.substring(1)));
				container.testAddLineItem(warehouse, cartLineItem);
			}
		}

		LineItemClassification classification = container.getAllClassifications().iterator().next();
		Collection<UniqueWhCombination> whCombinations = classification.generatePerfactCombinations();
		for (UniqueWhCombination uniqueWhCombination : whCombinations) {
			System.out.println(uniqueWhCombination.getBuckets());
		}
		
		System.out.println(System.currentTimeMillis() - time);
	}
}
