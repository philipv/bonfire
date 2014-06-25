package com.simulator.orderbook.util;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.PriorityQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.simulator.BaseUnitTest;
import com.simulator.data.Quote;
import com.simulator.data.Sequenceable;
import com.simulator.data.Side;
import com.simulator.data.Trade;
import com.simulator.factory.InjectionManager;
import com.simulator.util.MarketDepthImpl;

public class MarketDepthImplTest extends BaseUnitTest{

	private MarketDepthImpl marketDepth;
	
	private PriorityQueue<Sequenceable<Quote>> bidDepth;
	private PriorityQueue<Sequenceable<Quote>> askDepth;
	
	@Before
	public void init(){
		bidDepth = new InjectionManager().createPriorityQueue(Side.B);
		askDepth = new InjectionManager().createPriorityQueue(Side.S);
		when(mockInjectionManager.createPriorityQueue(Side.B)).thenReturn(bidDepth);
		when(mockInjectionManager.createPriorityQueue(Side.S)).thenReturn(askDepth);
		marketDepth = null;
	}
	
	@Test
	public void placeBuyQuote(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.B);
		double[] buyPricesInDesc = new double[]{10.1, 10, 9.9};
		int[][] buyQuantities = new int[][]{{100, 10}, {1000}, {100}};
		
		populateBook(buyPricesInDesc, buyQuantities);
		assertDepth(buyPricesInDesc, buyQuantities, bidDepth);
	}

	@Test
	public void placeSellQuote(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.S);
		double[] sellPricesInAsc = new double[]{9.9, 10.0};
		int[][] sellQuantities = new int[][]{{100, 10}, {1000}};
		
		populateBook(sellPricesInAsc, sellQuantities);
		assertDepth(sellPricesInAsc, sellQuantities, askDepth);
	}

	@Test
	public void matchSellQuoteWithResidualOnDepth(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.B);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{1000}, {100}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 50));
		assertTradeDetails(trades, new double[]{10.0}, new int[]{50});
		assertDepth(buyPricesInBook, new int[][]{{950}, {100}}, bidDepth);
	}
	
	@Test
	public void matchBuyQuoteWithResidualOnDepth(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.S);
		
		double[] sellPricesInBook = new double[]{10.0, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {200}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(10.0, 50));
		assertTradeDetails(trades, new double[]{9.9}, new int[]{50});
		assertDepth(new double[]{9.9, 10.0}, new int[][]{{150}, {1000}}, askDepth);
	}
	
	@Test
	public void matchSellQuoteOnMultiplePriceLevelWithResidualOnDepth(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.B);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{100}, {1000}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{10.0, 9.9}, new int[]{100, 100});
		assertDepth(new double[]{9.9}, new int[][]{{900}}, bidDepth);
	}
	
	@Test
	public void matchBuyQuoteOnMultiplePriceLevelWithResidualOnDepth(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.S);
		
		double[] sellPricesInBook = new double[]{10.0, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {200}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(10.0, 250));
		assertTradeDetails(trades, new double[]{9.9, 10.0}, new int[]{200, 50});
		assertDepth(new double[]{10.0}, new int[][]{{950}}, askDepth);
	}
	
	@Test
	public void matchSellQuoteOnMultipleQuotesOnSamePriceLevelWithResidualOnDepth(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.B);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{100, 150}, {1000}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{10, 10}, new int[]{100, 100});
		assertDepth(new double[]{10.0, 9.9}, new int[][]{{50}, {1000}}, bidDepth);
	}
	
	@Test
	public void matchBuyQuoteOnMultipleQuotesOnSamePriceLevelWithResidualOnDepth(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.S);
		
		double[] sellPricesInBook = new double[]{10, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {10, 260}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{9.9, 9.9}, new int[]{10, 190});
		assertDepth(new double[]{9.9, 10.0}, new int[][]{{70}, {1000}}, askDepth);
	}
	
	@Test
	public void matchWrongInput(){
		marketDepth = new MarketDepthImpl(mockInjectionManager, Side.S);
		
		double[] sellPricesInBook = new double[]{10, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {10, 260}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		Quote[] testQuotes = new Quote[]{new Quote(), createQuote(0, 10), createQuote(9.0001, 10), createQuote(10, 0)};
		for(Quote testQuote:testQuotes){
			try{
				marketDepth.match(testQuote);
				Assert.fail("Should not reach this point");
			}catch(Exception e){
				Assert.assertTrue(e instanceof IllegalArgumentException);
			}
		}
		assertDepth(new double[]{9.9, 10.0}, new int[][]{{10, 260}, {1000}}, askDepth);
	}
	
	private void populateBook(double[] buyPrices, int[][] buyQuantities) {
		for(int i=0;i<buyPrices.length;i++){
			for(int quantity:buyQuantities[i]){
				marketDepth.add(createQuote(buyPrices[i], quantity));
			}
		}
	}
}
