package com.simulator.orderbook;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.simulator.BaseUnitTest;
import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.data.Side;
import com.simulator.util.MarketDepthImpl;

public class OrderBookTest extends BaseUnitTest{

	private OrderBook orderBook;
	
	@Mock
	private MarketDepthImpl bids;
	
	@Mock
	private MarketDepthImpl asks;
	
	@Before
	public void init(){
		when(mockInjectionManager.createMarketDepth(Side.B)).thenReturn(bids);
		when(mockInjectionManager.createMarketDepth(Side.S)).thenReturn(asks);
		orderBook = new OrderBook(mockInjectionManager);
	}
	
	@After
	public void tearDown(){
		orderBook = null;
		bids = null;
		asks = null;
	}
	
	@Test
	public void testFirstBuyQuote(){
		Quote quote = createQuote(10.9, 250, Side.B);
		when(bids.add(quote)).thenReturn(true);
		MarketUpdate<Double, Long> orderBookUpdate = orderBook.placeOrder(quote);
		verify(asks, times(1)).match(quote);//try to match once
		verify(asks, times(0)).add(quote);
		verify(bids, times(0)).match(quote);
		verify(bids, times(1)).add(quote);//try to place once
		Assert.assertTrue(orderBookUpdate.getAskUpdates().isEmpty());
		Assert.assertEquals(1, orderBookUpdate.getBidUpdates().size());
		Assert.assertEquals(orderBookUpdate.getBidUpdates().get(quote.getPrice()), quote.getQuantity());
		System.out.println(orderBookUpdate.toString());
	}
	
	@Test
	public void testFirstSellQuote(){
		Quote quote = createQuote(10.9, 250, Side.S);
		when(asks.add(quote)).thenReturn(true);
		MarketUpdate<Double, Long> orderBookUpdate = orderBook.placeOrder(quote);
		verify(bids, times(1)).match(quote);
		verify(bids, times(0)).add(quote);
		verify(asks, times(0)).match(quote);
		verify(asks, times(1)).add(quote);
		Assert.assertTrue(orderBookUpdate.getBidUpdates().isEmpty());
		Assert.assertEquals(1, orderBookUpdate.getAskUpdates().size());
		Assert.assertEquals(orderBookUpdate.getAskUpdates().get(quote.getPrice()), quote.getQuantity());
		System.out.println(orderBookUpdate.toString());
	}
	
	@Test
	public void testMatchSellQuote(){
		when(bids.add(any(Quote.class))).thenReturn(true);
		MarketUpdate<Double, Long> orderBookUpdate = orderBook.placeOrder(createQuote(11.0, 75, Side.B));
		Assert.assertTrue(orderBookUpdate.getBidUpdates().size()==1);
		orderBookUpdate = orderBook.placeOrder(createQuote(10.9, 175, Side.B));
		Assert.assertTrue(orderBookUpdate.getBidUpdates().size()==2);
		Quote quote = createQuote(10.9, 250, Side.S);
		when(asks.add(quote)).thenReturn(false);
		when(bids.match(quote)).thenReturn(Arrays.asList(createTrade(11.0, 75), createTrade(10.9, 175)));
		orderBookUpdate = orderBook.placeOrder(quote);
		verify(bids, times(1)).match(quote);
		verify(bids, times(0)).add(quote);
		verify(asks, times(0)).match(quote);
		verify(asks, times(1)).add(quote);
		Assert.assertTrue(orderBookUpdate.getBidUpdates().isEmpty());
		Assert.assertTrue(orderBookUpdate.getAskUpdates().isEmpty());
		assertTradeDetails(orderBookUpdate.getTrades(), new double[]{11.0, 10.9},  new int[]{75, 175});
		System.out.println(orderBookUpdate.toString());
	}
	
	@Test
	public void testMatchBuyQuote(){
		when(asks.add(any(Quote.class))).thenReturn(true);
		MarketUpdate<Double, Long> orderBookUpdate = orderBook.placeOrder(createQuote(11.0, 75, Side.S));
		Assert.assertTrue(orderBookUpdate.getAskUpdates().size()==1);
		orderBookUpdate = orderBook.placeOrder(createQuote(10.9, 175, Side.S));
		Assert.assertTrue(orderBookUpdate.getAskUpdates().size()==2);
		Quote quote = createQuote(11.0, 250, Side.B);
		when(bids.add(quote)).thenReturn(false);
		when(asks.match(quote)).thenReturn(Arrays.asList(createTrade(10.9, 175), createTrade(11.0, 70)));
		orderBookUpdate = orderBook.placeOrder(quote);
		verify(bids, times(0)).match(quote);
		verify(bids, times(1)).add(quote);
		verify(asks, times(1)).match(quote);
		verify(asks, times(0)).add(quote);
		Assert.assertTrue(orderBookUpdate.getBidUpdates().isEmpty());
		Assert.assertTrue(orderBookUpdate.getAskUpdates().size()==1);
		Assert.assertEquals(5, orderBookUpdate.getAskUpdates().get(11.0).longValue());
		assertTradeDetails(orderBookUpdate.getTrades(), new double[]{10.9, 11.0},  new int[]{175, 70});
		System.out.println(orderBookUpdate.toString());
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
			orderBook.placeOrder(quote);
		}catch(Exception e){
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
		verify(bids, times(0)).add(quote);
		verify(asks, times(0)).add(quote);
		
	}
}
