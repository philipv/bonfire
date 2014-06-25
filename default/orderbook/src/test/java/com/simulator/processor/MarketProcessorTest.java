package com.simulator.processor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.simulator.BaseUnitTest;
import com.simulator.data.Quote;
import com.simulator.exception.ProcessingFailedException;
import com.simulator.orderbook.OrderBook;

public class MarketProcessorTest extends BaseUnitTest{
	private MarketProcessor marketProcessor;
	
	@Mock
	private Map<String, OrderBook> orderBooks;
	
	@Before
	public void init(){
		when(mockInjectionManager.createOrderBookPerSymbol()).thenReturn(orderBooks);
		marketProcessor = new MarketProcessor(mockInjectionManager);
	}
	
	@Test
	public void testRepetitiveQuote() throws ProcessingFailedException{
		OrderBook mockOrderBook = mock(OrderBook.class);
		when(mockInjectionManager.createOrderBook()).thenReturn(mockOrderBook);
		when(orderBooks.get(null)).thenReturn(null, mockOrderBook);
		Quote newQuote = createQuote(10.0, 25);
		marketProcessor.createMarketOrder(newQuote);
		verify(orderBooks, times(1)).get(any(String.class));
		verify(orderBooks, times(1)).put(any(String.class), any(OrderBook.class));
		verify(mockInjectionManager, times(1)).createOrderBook();
		verify(mockOrderBook, times(1)).placeOrder(newQuote);
		marketProcessor.createMarketOrder(newQuote);
		verify(orderBooks, times(2)).get(any(String.class));
		verify(orderBooks, times(1)).put(any(String.class), any(OrderBook.class));
		verify(mockInjectionManager, times(1)).createOrderBook();
		verify(mockOrderBook, times(2)).placeOrder(newQuote);
	}
	
	@Test
	public void testRuntimeExceptionCase(){
		Quote newQuote = createQuote(10.0, 25);
		for(Exception exception:new Exception[]{new RuntimeException(), new IllegalArgumentException()}){
			OrderBook mockOrderBook = mock(OrderBook.class);
			when(mockInjectionManager.createOrderBook()).thenReturn(mockOrderBook);
			when(orderBooks.get(null)).thenReturn(mockOrderBook);
			when(mockOrderBook.placeOrder(newQuote)).thenThrow(exception);
			try{
				marketProcessor.createMarketOrder(newQuote);
				Assert.fail();
			}catch(Exception ex){
				Assert.assertTrue(ex instanceof ProcessingFailedException);
			}
			
		}
	}
	
}
