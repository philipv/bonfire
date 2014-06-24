package com.simulator.orderbook;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.simulator.BaseUnitTest;
import com.simulator.data.OrderBookUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Side;
import com.simulator.util.MarketDepth;

public class OrderBookTest extends BaseUnitTest{

	private OrderBook orderBook;
	
	private MarketDepth bids;
	private MarketDepth asks;
	
	@Before
	public void init(){
		bids = mock(MarketDepth.class);
		asks = mock(MarketDepth.class);
		orderBook = new OrderBook(bids, asks);
	}
	
	@After
	public void tearDown(){
		orderBook = null;
		bids = null;
		asks = null;
	}
	
	@Test
	public void testMatchNewBuyQuote(){
		Quote quote = createQuote(10.9, 250, Side.B);
		OrderBookUpdate<Double, Integer> orderBookUpdate = orderBook.placeOrder(quote);
		verify(asks, times(1)).match(quote);//try to match once
		verify(bids, times(1)).add(quote);//try to place once
		verify(asks, times(0)).add(quote);
		verify(bids, times(0)).match(quote);
	}
	
	@Test
	public void testMatchNewSellQuote(){
		Quote quote = createQuote(10.9, 250, Side.S);
		orderBook.placeOrder(quote);
		verify(bids, times(1)).match(quote);
		verify(asks, times(0)).match(quote);
	}
	
	@Test
	public void testFirstBuyQuote(){
		Quote quote = createQuote(10.9, 250, Side.B);
		OrderBookUpdate<Double, Integer> orderBookUpdate = orderBook.placeOrder(quote);
		verify(bids, times(1)).add(quote);
		verify(asks, times(0)).add(quote);
	}
	
	@Test
	public void testFirstSellQuote(){
		Quote quote = createQuote(10.9, 250, Side.S);
		OrderBookUpdate<Double, Integer> orderBookUpdate = orderBook.placeOrder(quote);
		verify(asks, times(1)).add(quote);
		verify(bids, times(0)).add(quote);
	}
	
	@Test
	public void testMatchNewInvalidSideQuote(){
		Quote quote = createQuote(10.9, 250, null);
		try{
			orderBook.placeOrder(quote);
			Assert.fail("should not reach this point");
		}catch(Exception e){
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		verify(bids, times(0)).match(quote);
		verify(asks, times(0)).match(quote);
	}
	
	@Test
	public void testPlaceNewInvalidSideQuote(){
		Quote quote = createQuote(10.9, 250, null);
		try{
			OrderBookUpdate<Double, Integer> orderBookUpdate = orderBook.placeOrder(quote);
		}catch(Exception e){
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		verify(bids, times(0)).add(quote);
		verify(asks, times(0)).add(quote);
		
	}
}
