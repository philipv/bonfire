package com.simulator.orderbook.util;

import org.junit.Assert;
import org.junit.Test;

import com.simulator.BaseUnitTest;
import com.simulator.data.Quote;
import com.simulator.data.Sequenceable;
import com.simulator.util.DescendingComparator;

public class DescendingComparatorTest extends BaseUnitTest{
	@Test
	public void testNonNullNonEqualValues(){
		int compareResult = new DescendingComparator<>().compare(new Sequenceable<Quote>(createQuote(24.0, 0)), 
				new Sequenceable<Quote>(createQuote(22.0, 0)));
		Assert.assertEquals(-1, compareResult);
	}
	
	@Test
	public void testNonNullEqualValuesWithNormalSequence(){
		int compareResult = new DescendingComparator<>().compare(new Sequenceable<Quote>(createQuote(24.0, 0)), 
				new Sequenceable<Quote>(createQuote(24.0, 0)));
		Assert.assertEquals(-1, compareResult);//since second quote is created after first
	}
	
	@Test
	public void testNonNullEqualValuesWithReverseSequence(){
		Sequenceable<Quote> seq1 = new Sequenceable<Quote>(createQuote(24.0, 0));
		Sequenceable<Quote> seq2 = new Sequenceable<Quote>(createQuote(24.0, 0));
		int compareResult = new DescendingComparator<>().compare(seq2, 
				seq1);
		Assert.assertEquals(1, compareResult);//since second quote is created before first
	}
	
	@Test
	public void testNonNullEqualValuesWithSameSequence(){
		Sequenceable<Quote> seq1 = new Sequenceable<Quote>(createQuote(24.0, 0));
		int compareResult = new DescendingComparator<>().compare(seq1, 
				seq1);
		Assert.assertEquals(0, compareResult);//since both are same
	}
	
	@Test
	public void testWithNullFirstValue(){
		try{
			new DescendingComparator<>().compare(null, 
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
			new DescendingComparator<>().compare(new Sequenceable<Quote>(createQuote(22.0, 0)),
					null);
			Assert.fail("Should not reach this point");
		}catch(Exception e){
			e.printStackTrace();
			Assert.assertTrue(e instanceof NullPointerException);
		}
	}
}
