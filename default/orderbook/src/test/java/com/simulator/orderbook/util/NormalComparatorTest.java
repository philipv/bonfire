package com.simulator.orderbook.util;

import org.junit.Assert;
import org.junit.Test;

import com.simulator.orderbook.BaseUnitTest;
import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Sequenceable;

public class NormalComparatorTest extends BaseUnitTest{
	@Test
	public void testNonNullNonEqualValues(){
		int compareResult = new NormalComparator<>().compare(new Sequenceable<Quote>(createQuote(24.0, 0)), 
				new Sequenceable<Quote>(createQuote(22.0, 0)));
		Assert.assertEquals(1, compareResult);
	}
	
	@Test
	public void testNonNullEqualValuesWithNormalSequence(){
		int compareResult = new NormalComparator<>().compare(new Sequenceable<Quote>(createQuote(24.0, 0)), 
				new Sequenceable<Quote>(createQuote(24.0, 0)));
		Assert.assertEquals(-1, compareResult);//since second quote is created after first
	}
	
	@Test
	public void testNonNullEqualValuesWithReverseSequence(){
		Sequenceable<Quote> seq1 = new Sequenceable<Quote>(createQuote(24.0, 0));
		Sequenceable<Quote> seq2 = new Sequenceable<Quote>(createQuote(24.0, 0));
		int compareResult = new NormalComparator<>().compare(seq2, 
				seq1);
		Assert.assertEquals(1, compareResult);//since second quote is created before first
	}
	
	@Test
	public void testNonNullEqualValuesWithSameSequence(){
		Sequenceable<Quote> seq1 = new Sequenceable<Quote>(createQuote(24.0, 0));
		int compareResult = new NormalComparator<>().compare(seq1, 
				seq1);
		Assert.assertEquals(0, compareResult);//since both are same
	}
	
	@Test
	public void testWithNullFirstValue(){
		try{
			new NormalComparator<>().compare(null, 
					new Sequenceable<Quote>(createQuote(22.0, 0)));
			Assert.fail("Should not reach this point");
		}catch(Exception e){
			e.printStackTrace();
			Assert.assertTrue(e instanceof NullPointerException);
		}
	}
	@Test
	public void testWithNullSecondValue(){
		try{
			new NormalComparator<>().compare(new Sequenceable<Quote>(createQuote(22.0, 0)),
					null);
			Assert.fail("Should not reach this point");
		}catch(Exception e){
			e.printStackTrace();
			Assert.assertTrue(e instanceof NullPointerException);
		}
	}
}
