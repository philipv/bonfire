package com.simulator.orderbook;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Trade;

public class OrderBook {
	private TreeMap<Double, List<Quote>> bids;
	private TreeMap<Double, List<Quote>> asks;
	
	public OrderBook(TreeMap<Double, List<Quote>> bids,
			TreeMap<Double, List<Quote>> asks) {
		super();
		this.bids = bids;
		this.asks = asks;
	}
	
	public List<Trade> add(Quote newQuote){
		SortedMap<Double, List<Quote>> matchablePriceLevels = new TreeMap<>();
		switch(newQuote.getSide()){
			case B:
				matchablePriceLevels = asks.headMap(newQuote.getPrice());
				break;
			case S:
				matchablePriceLevels = bids.headMap(newQuote.getPrice());
				break;
		}
		List<Trade> trades = match(newQuote, matchablePriceLevels);
		placeQuote(newQuote);
		return trades;
	}

	private List<Trade> match(Quote newQuote, SortedMap<Double, List<Quote>> matchablePriceLevels) {
		List<Trade> trades = new LinkedList<>();
		Iterator<Entry<Double, List<Quote>>> priceLevelsIterator = matchablePriceLevels.entrySet().iterator();
		while(priceLevelsIterator.hasNext()){
			
			if(newQuote.getQuantity()>0){
				Entry<Double, List<Quote>> priceLevel = priceLevelsIterator.next();
				List<Quote> quotesOnBook = priceLevel.getValue();
				Iterator<Quote> quotesOnBookIterator = quotesOnBook.iterator();
				
				while(quotesOnBookIterator.hasNext()){
					
					if(newQuote.getQuantity()>0){
						Quote quoteOnBook = quotesOnBookIterator.next();
						Trade trade = new Trade();
						trade.setPrice(priceLevel.getKey());
						
						if(newQuote.getQuantity()>quoteOnBook.getQuantity()){
							int unfilledQuantity = newQuote.getQuantity() - quoteOnBook.getQuantity();
							trade.setQuantity(quoteOnBook.getQuantity());
							newQuote.setQuantity(unfilledQuantity);
							quotesOnBookIterator.remove();
						}else{
							int unfilledQuantity = quoteOnBook.getQuantity() - newQuote.getQuantity();
							trade.setQuantity(newQuote.getQuantity());
							newQuote.setQuantity(0);
							quoteOnBook.setQuantity(unfilledQuantity);
						}
						
						trades.add(trade);
					}
				}
				
				if(quotesOnBook.size()==0){
					priceLevelsIterator.remove();
				}
			}
			
		}
		
		return trades;
	}
	
	private void placeQuote(Quote newQuote){
		if(newQuote.getQuantity()>0){
			switch (newQuote.getSide()) {
				case B:
					placeQuote(newQuote, bids);
					break;
				case S:
					placeQuote(newQuote, asks);
					break;
			}
		}
	}

	private void placeQuote(Quote newQuote,
			TreeMap<Double, List<Quote>> targetBook) {
		List<Quote> quotesOnBook = targetBook.get(newQuote.getPrice());
		if(quotesOnBook==null){
			quotesOnBook = new LinkedList<>();
			targetBook.put(newQuote.getPrice(), quotesOnBook);
		}
		quotesOnBook.add(newQuote);
	}
}
