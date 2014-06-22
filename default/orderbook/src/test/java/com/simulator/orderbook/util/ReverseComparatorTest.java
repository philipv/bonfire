package com.simulator.orderbook.util;

import org.junit.Assert;
import org.junit.Test;

public class ReverseComparatorTest {
	@Test
	public void testNonNullNonEqualValues(){
		int compareResult = new ReverseComparator<Double>().compare(24.0, 22.0);
		Assert.assertEquals(-1, compareResult);
	}
	
	@Test
	public void testNonNullEqualValues(){
		int compareResult = new ReverseComparator<Double>().compare(24.0, 24.0);
		Assert.assertEquals(0, compareResult);
	}
	
	@Test
	public void testWithNullFirstValue(){
		try{
			new ReverseComparator<Double>().compare(null, 24.0);
			Assert.fail("Should not reach this point");
		}catch(Exception e){
			Assert.assertTrue(e instanceof NullPointerException);
		}
	}
}
