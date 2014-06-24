package com.simulator.orderbook.util;

import java.util.List;
import java.util.PriorityQueue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.simulator.BaseUnitTest;
import com.simulator.data.Quote;
import com.simulator.data.Sequenceable;
import com.simulator.data.Side;
import com.simulator.data.Trade;
import com.simulator.util.AscendingComparator;
import com.simulator.util.DescendingComparator;
import com.simulator.util.MarketDepth;

public class MarketDepthTest extends BaseUnitTest{

	private MarketDepth marketDepth;
	
	private PriorityQueue<Sequenceable<Quote>> depth;
	
	@After
	public void after(){
		depth = null;
		marketDepth = null;
	}
	
	@Test
	public void placeBuyQuote(){
		depth = new PriorityQueue<>(16, new DescendingComparator<>());
		marketDepth = new MarketDepth(depth, Side.B);
		double[] buyPricesInDesc = new double[]{10.1, 10, 9.9};
		int[][] buyQuantities = new int[][]{{100, 10}, {1000}, {100}};
		
		populateBook(buyPricesInDesc, buyQuantities);
		assertDepth(buyPricesInDesc, buyQuantities, depth);
	}

	@Test
	public void placeSellQuote(){
		depth = new PriorityQueue<>();
		marketDepth = new MarketDepth(depth, Side.S);
		double[] sellPricesInAsc = new double[]{9.9, 10.0};
		int[][] sellQuantities = new int[][]{{100, 10}, {1000}};
		
		populateBook(sellPricesInAsc, sellQuantities);
		assertDepth(sellPricesInAsc, sellQuantities, depth);
	}

	@Test
	public void matchSellQuoteWithResidualOnDepth(){
		depth = new PriorityQueue<>(16, new DescendingComparator<>());
		marketDepth = new MarketDepth(depth, Side.B);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{1000}, {100}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 50));
		assertTradeDetails(trades, new double[]{10.0}, new int[]{50});
		assertDepth(buyPricesInBook, new int[][]{{950}, {100}}, depth);
	}
	
	@Test
	public void matchBuyQuoteWithResidualOnDepth(){
		depth = new PriorityQueue<>(16, new AscendingComparator<>());
		marketDepth = new MarketDepth(depth, Side.S);
		
		double[] sellPricesInBook = new double[]{10.0, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {200}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(10.0, 50));
		assertTradeDetails(trades, new double[]{9.9}, new int[]{50});
		assertDepth(new double[]{9.9, 10.0}, new int[][]{{150}, {1000}}, depth);
	}
	
	@Test
	public void matchSellQuoteOnMultiplePriceLevelWithResidualOnDepth(){
		depth = new PriorityQueue<>(16, new DescendingComparator<>());
		marketDepth = new MarketDepth(depth, Side.B);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{100}, {1000}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{10.0, 9.9}, new int[]{100, 100});
		assertDepth(new double[]{9.9}, new int[][]{{900}}, depth);
	}
	
	@Test
	public void matchBuyQuoteOnMultiplePriceLevelWithResidualOnDepth(){
		depth = new PriorityQueue<>(16, new AscendingComparator<>());
		marketDepth = new MarketDepth(depth, Side.S);
		
		double[] sellPricesInBook = new double[]{10.0, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {200}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(10.0, 250));
		assertTradeDetails(trades, new double[]{9.9, 10.0}, new int[]{200, 50});
		assertDepth(new double[]{10.0}, new int[][]{{950}}, depth);
	}
	
	@Test
	public void matchSellQuoteOnMultipleQuotesOnSamePriceLevelWithResidualOnDepth(){
		depth = new PriorityQueue<>(16, new DescendingComparator<>());
		marketDepth = new MarketDepth(depth, Side.B);
		
		double[] buyPricesInBook = new double[]{10, 9.9};
		int[][] buyQuantitiesInBook = new int[][]{{100, 150}, {1000}};
		populateBook(buyPricesInBook, buyQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{10, 10}, new int[]{100, 100});
		assertDepth(new double[]{10.0, 9.9}, new int[][]{{50}, {1000}}, depth);
	}
	
	@Test
	public void matchBuyQuoteOnMultipleQuotesOnSamePriceLevelWithResidualOnDepth(){
		depth = new PriorityQueue<>(16, new AscendingComparator<>());
		marketDepth = new MarketDepth(depth, Side.S);
		
		double[] sellPricesInBook = new double[]{10, 9.9};
		int[][] sellQuantitiesInBook = new int[][]{{1000}, {10, 260}};
		populateBook(sellPricesInBook, sellQuantitiesInBook);
		List<Trade> trades = marketDepth.match(createQuote(9.9, 200));
		assertTradeDetails(trades, new double[]{9.9, 9.9}, new int[]{10, 190});
		assertDepth(new double[]{9.9, 10.0}, new int[][]{{70}, {1000}}, depth);
	}
	
	@Test
	public void testWrongInput(){
		depth = new PriorityQueue<>(16, new AscendingComparator<>());
		marketDepth = new MarketDepth(depth, Side.S);
		
		Quote quote = createQuote(9.9, 200);
		quote.setPrice(null);
		try{
			marketDepth.match(quote);
			Assert.fail("Should not reach this point");
		}catch(Exception e){
			Assert.assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	private void populateBook(double[] buyPrices, int[][] buyQuantities) {
		for(int i=0;i<buyPrices.length;i++){
			for(int quantity:buyQuantities[i]){
				marketDepth.add(createQuote(buyPrices[i], quantity));
			}
		}
	}
}
