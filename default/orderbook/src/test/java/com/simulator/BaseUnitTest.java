package com.simulator;

import java.util.List;
import java.util.PriorityQueue;

import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.simulator.data.Quote;
import com.simulator.data.Sequenceable;
import com.simulator.data.Side;
import com.simulator.data.Trade;
import com.simulator.factory.InjectionManager;


public class BaseUnitTest {
	
	@Mock
	protected InjectionManager mockInjectionManager;
	
	protected Quote createQuote(double price, long quantity){
		Quote quote = new Quote();
		quote.setPrice(price);
		quote.setQuantity(quantity);
		return quote;
	}
	
	protected Trade createTrade(double price, long quantity){
		Trade trade = new Trade();
		trade.setPrice(price);
		trade.setQuantity(quantity);
		return trade;
	}
	
	protected Quote createQuote(double price, long quantity, Side side){
		Quote quote = createQuote(price, quantity);
		quote.setSide(side);
		return quote;
	}
	
	@Before
	public void baseInit(){
		MockitoAnnotations.initMocks(this);
	}
	
	protected void assertTradeDetails(List<Trade> trades, double[] expectedPrices, int[] expectedQuantities){
		Assert.assertTrue(expectedPrices.length==expectedQuantities.length);
		Assert.assertNotNull(trades);
		Assert.assertTrue(expectedPrices.length==trades.size());
		for(int i=0; i<expectedPrices.length;i++){
			Trade trade = trades.get(i);
			Assert.assertNotNull(trade);
			Assert.assertEquals(expectedPrices[i],trade.getPrice(), 0.0001);
			Assert.assertEquals(expectedQuantities[i], trade.getQuantity().intValue());
		}
	}
	
	protected void assertDepth(double[] sortedExpectedPrices, int[][] quantities,
			PriorityQueue<Sequenceable<Quote>> depth) {
		for(int i=0;i<sortedExpectedPrices.length;i++){
			for(int j=0;j<quantities[i].length;j++){
				Quote quote = depth.poll().getEntry();
				Assert.assertNotNull(quote);
				Assert.assertEquals(sortedExpectedPrices[i], quote.getPrice(), 0.0001);
				Assert.assertEquals(sortedExpectedPrices[i], quote.getPrice(), 0.0001);
				Assert.assertEquals(quantities[i][j], quote.getQuantity().intValue());
			}
		}
		Assert.assertEquals(0, depth.size());
	}
}
