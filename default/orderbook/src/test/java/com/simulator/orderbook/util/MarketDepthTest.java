package com.simulator.orderbook.util;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.simulator.orderbook.BaseUnitTest;
import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Trade;

public class MarketDepthTest extends BaseUnitTest{

	private MarketDepth marketDepth;
	
	private TreeMap<Double, List<Quote>> depth;
	
	@After
	public void after(){
		depth = null;
		marketDepth = null;
	}
	
	@Test
	public void placeBuyQuote(){
		depth = new TreeMap<>(new ReverseComparator<Double>());
		marketDepth = new MarketDepth(depth);
		double[] buyPricesInDesc = new double[]{10.1, 10, 9.9};
		int[][] buyQuantities = new int[][]{{100, 10}, {1000}, {100}};
		
		populateBook(buyPricesInDesc, buyQuantities);
		assertDepth(buyPricesInDesc, buyQuantities, depth);
	}

	@Test
	public void placeSellQuote(){
		depth = new TreeMap<>();
		marketDepth = new MarketDepth(depth);
		double[] sellPricesInAsc = new double[]{9.9, 10.0};
		int[][] sellQuantities = new int[][]{{100, 10}, {1000}};
		
		populateBook(sellPricesInAsc, sellQuantities);
		assertDepth(sellPricesInAsc, sellQuantities, depth);
	}

	@Test
	public void matchSellQuoteWithResidualOnDepth(){
		depth = new TreeMap<>(new ReverseComparator<Double>());
		marketDepth = new MarketDepth(depth);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{1000}, {100}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 50));
		assertTradeDetails(trades, new double[]{10.0}, new int[]{50});
		assertDepth(buyPricesInBook, new int[][]{{950}, {100}}, depth);
	}
	
	@Test
	public void matchBuyQuoteWithResidualOnDepth(){
		depth = new TreeMap<>();
		marketDepth = new MarketDepth(depth);
		
		double[] sellPricesInBook = new double[]{10.0, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {200}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(10.0, 50));
		assertTradeDetails(trades, new double[]{9.9}, new int[]{50});
		assertDepth(new double[]{9.9, 10.0}, new int[][]{{150}, {1000}}, depth);
	}
	
	@Test
	public void matchSellQuoteOnMultiplePriceLevelWithResidualOnDepth(){
		depth = new TreeMap<>(new ReverseComparator<Double>());
		marketDepth = new MarketDepth(depth);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{100}, {1000}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{10.0, 9.9}, new int[]{100, 100});
		assertDepth(new double[]{9.9}, new int[][]{{900}}, depth);
	}
	
	@Test
	public void matchBuyQuoteOnMultiplePriceLevelWithResidualOnDepth(){
		depth = new TreeMap<>();
		marketDepth = new MarketDepth(depth);
		
		double[] sellPricesInBook = new double[]{10.0, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {200}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(10.0, 250));
		assertTradeDetails(trades, new double[]{9.9, 10.0}, new int[]{200, 50});
		assertDepth(new double[]{10.0}, new int[][]{{950}}, depth);
	}
	
	@Test
	public void matchSellQuoteOnMultipleQuotesOnSamePriceLevelWithResidualOnDepth(){
		depth = new TreeMap<>(new ReverseComparator<Double>());
		marketDepth = new MarketDepth(depth);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{100, 150}, {1000}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{10, 10}, new int[]{100, 100});
		assertDepth(new double[]{10.0, 9.9}, new int[][]{{50}, {1000}}, depth);
	}
	
	@Test
	public void matchBuyQuoteOnMultipleQuotesOnSamePriceLevelWithResidualOnDepth(){
		depth = new TreeMap<>();
		marketDepth = new MarketDepth(depth);
		
		double[] sellPricesInBook = new double[]{10, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {10, 260}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{9.9, 9.9}, new int[]{10, 190});
		assertDepth(new double[]{9.9, 10.0}, new int[][]{{70}, {1000}}, depth);
	}
	
	private void populateBook(double[] buyPrices, int[][] buyQuantities) {
		for(int i=0;i<buyPrices.length;i++){
			for(int quantity:buyQuantities[i]){
				marketDepth.placeQuote(createQuote(buyPrices[i], quantity));
			}
		}
	}
	
	private void assertTradeDetails(List<Trade> trades, double[] expectedPrices, int[] expectedQuantities){
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
	
	private void assertDepth(double[] sortedExpectedPrices, int[][] quantities,
			TreeMap<Double, List<Quote>> depth) {
		Assert.assertEquals(sortedExpectedPrices.length, depth.size());
		Set<Entry<Double, List<Quote>>> priceLevels = depth.entrySet();
		int i=0;
		for(Entry<Double, List<Quote>> priceLevel:priceLevels){
			Assert.assertEquals(sortedExpectedPrices[i], priceLevel.getKey(), 0.0001);

			List<Quote> quotes = priceLevel.getValue();
			Assert.assertNotNull(quotes);
			Assert.assertEquals(quantities[i].length, quotes.size());
			for(int j=0;j<quantities[i].length;j++){
				Quote quote = quotes.get(j);
				Assert.assertNotNull(quote);
				Assert.assertEquals(sortedExpectedPrices[i], quote.getPrice(), 0.0001);
				Assert.assertEquals(quantities[i][j], quote.getQuantity().intValue());
			}
			i++;
		}
	}
}
