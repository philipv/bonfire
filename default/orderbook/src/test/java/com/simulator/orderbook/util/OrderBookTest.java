package com.simulator.orderbook.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.simulator.orderbook.BaseUnitTest;
import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Side;

import static org.mockito.Mockito.*;

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
		orderBook.match(quote);
		verify(asks, times(1)).match(quote);
		verify(bids, times(0)).match(quote);
	}
	
	@Test
	public void testMatchNewSellQuote(){
		Quote quote = createQuote(10.9, 250, Side.S);
		orderBook.match(quote);
		verify(bids, times(1)).match(quote);
		verify(asks, times(0)).match(quote);
	}
	
	@Test
	public void testFirstBuyQuote(){
		Quote quote = createQuote(10.9, 250, Side.B);
		orderBook.place(quote);
		verify(bids, times(1)).placeQuote(quote);
		verify(asks, times(0)).placeQuote(quote);
	}
	
	@Test
	public void testFirstSellQuote(){
		Quote quote = createQuote(10.9, 250, Side.S);
		orderBook.place(quote);
		verify(asks, times(1)).placeQuote(quote);
		verify(bids, times(0)).placeQuote(quote);
	}
	
	@Test
	public void testMatchNewInvalidSideQuote(){
		Quote quote = createQuote(10.9, 250, null);
		try{
			orderBook.match(quote);
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
			orderBook.place(quote);
		}catch(Exception e){
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		verify(bids, times(0)).placeQuote(quote);
		verify(asks, times(0)).placeQuote(quote);
		
	}
}
