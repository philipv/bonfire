package com.bonfire;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.Assert;
import org.junit.Before;

public class BaseTest {
	protected ConcurrentHashMap<String, Double> positions = new ConcurrentHashMap<String, Double>();
	
	@Before
	public void reset(){
		positions.clear();
	}
	
	protected void assertAggregatedResult(String currency, double expectedValue) {
		Double position = positions.get(currency);
		Assert.assertNotNull(position);
		Assert.assertEquals(expectedValue, position.doubleValue(), 0.0d);
	}
	
	protected void assertMissingResult(String currency) {
		Double position = positions.get(currency);
		Assert.assertNull(position);
	}
}
