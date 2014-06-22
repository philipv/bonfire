package com.simulator.orderbook.util;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.simulator.orderbook.BaseUnitTest;
import com.simulator.orderbook.ReverseComparator;
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
		double[] buyPricesInOrder = new double[]{10.1, 10, 9.9};
		int[][] buyQuantities = new int[][]{{100, 10}, {1000}, {100}};
		
		populateBook(buyPricesInOrder, buyQuantities);
		
		Assert.assertEquals(3, depth.size());
		
		Set<Entry<Double, List<Quote>>> priceLevels = depth.entrySet();
		
		int i=0;
		for(Entry<Double, List<Quote>> priceLevel:priceLevels){
			List<Quote> quotes = priceLevel.getValue();
			Assert.assertEquals(buyPricesInOrder[i], priceLevel.getKey(), 0.0001);
			
			Assert.assertNotNull(quotes);
			Assert.assertEquals(buyQuantities[i].length, quotes.size());
			for(int j=0;j<buyQuantities[i].length;j++){
				Quote quote = quotes.get(j);
				Assert.assertNotNull(quote);
				Assert.assertEquals(buyPricesInOrder[i], quote.getPrice(), 0.0001);
				Assert.assertEquals(buyQuantities[i][j], quote.getQuantity().intValue());
			}
			i++;
		}
		Assert.assertEquals(3, i);
	}
	
	@Test
	public void placeSellQuote(){
		depth = new TreeMap<>();
		marketDepth = new MarketDepth(depth);
		double[] sellPricesInOrder = new double[]{9.9, 10.0};
		int[][] sellQuantities = new int[][]{{100, 10}, {1000}};
		
		populateBook(sellPricesInOrder, sellQuantities);
		
		Assert.assertEquals(2, depth.size());
		
		Set<Entry<Double, List<Quote>>> priceLevels = depth.entrySet();
		
		int i=0;
		for(Entry<Double, List<Quote>> priceLevel:priceLevels){
			List<Quote> quotes = priceLevel.getValue();
			Assert.assertEquals(sellPricesInOrder[i], priceLevel.getKey(), 0.0001);
			
			Assert.assertNotNull(quotes);
			Assert.assertEquals(sellQuantities[i].length, quotes.size());
			for(int j=0;j<sellQuantities[i].length;j++){
				Quote quote = quotes.get(j);
				Assert.assertNotNull(quote);
				Assert.assertEquals(sellPricesInOrder[i], quote.getPrice(), 0.0001);
				Assert.assertEquals(sellQuantities[i][j], quote.getQuantity().intValue());
			}
			i++;
		}
		Assert.assertEquals(2, i);
	}

	@Test
	public void matchSimpleSellQuote(){
		depth = new TreeMap<>(new ReverseComparator<Double>());
		marketDepth = new MarketDepth(depth);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{1000}, {100}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 50));
		Assert.assertNotNull(trades);
		Assert.assertEquals(1, trades.size());
		Trade trade = trades.get(0);
		Assert.assertEquals(50, trade.getQuantity().intValue());
		Assert.assertEquals(10, trade.getPrice(), 0.0001);
		List<Quote> priceLevel = depth.get(trade.getPrice());
		Assert.assertNotNull(priceLevel);
		Assert.assertEquals(1, priceLevel.size());
		Assert.assertEquals(950, priceLevel.get(0).getQuantity().intValue());
	}
	
	@Test
	public void matchSimpleBuyQuote(){
		depth = new TreeMap<>();
		marketDepth = new MarketDepth(depth);
		
		double[] sellPricesInBook = new double[]{10.0, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {100}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(10.0, 50));
		Assert.assertNotNull(trades);
		Assert.assertEquals(1, trades.size());
		Trade trade = trades.get(0);
		Assert.assertEquals(50, trade.getQuantity().intValue());
		Assert.assertEquals(9.9, trade.getPrice(), 0.0001);
		List<Quote> priceLevel = depth.get(trade.getPrice());
		Assert.assertNotNull(priceLevel);
		Assert.assertEquals(1, priceLevel.size());
		Assert.assertEquals(50, priceLevel.get(0).getQuantity().intValue());
	}
	
	private void populateBook(double[] buyPrices, int[][] buyQuantities) {
		for(int i=0;i<buyPrices.length;i++){
			for(int quantity:buyQuantities[i]){
				marketDepth.placeQuote(createQuote(buyPrices[i], quantity));
			}
		}
	}
	
	
	private Quote createQuote(double price, int quantity){
		Quote quote = new Quote();
		quote.setPrice(price);
		quote.setQuantity(quantity);
		return quote;
	}
}
