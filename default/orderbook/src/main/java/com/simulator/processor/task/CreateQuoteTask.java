package com.simulator.processor.task;

import java.util.concurrent.Callable;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.processor.MarketProcessor;

public class CreateQuoteTask implements Callable<MarketUpdate<Double, Integer>> {
	private final Quote newQuote;
	private MarketProcessor marketProcessor;

	public CreateQuoteTask(MarketProcessor marketProcessor, Quote newQuote) {
		this.marketProcessor = marketProcessor;
		this.newQuote = newQuote;
	}

	@Override
	public MarketUpdate<Double, Integer> call() throws Exception {
		MarketUpdate<Double, Integer> result = marketProcessor
				.createMarketOrder(newQuote);
		return result;
	}
}