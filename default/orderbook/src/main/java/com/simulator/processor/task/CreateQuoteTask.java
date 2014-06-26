package com.simulator.processor.task;

import java.util.concurrent.Callable;

import com.simulator.data.MarketUpdate;
import com.simulator.data.Quote;
import com.simulator.processor.IProcessor;

public class CreateQuoteTask implements Callable<MarketUpdate<Double, Long>> {
	private final Quote newQuote;
	private IProcessor<Quote, MarketUpdate<Double, Long>> marketProcessor;

	public CreateQuoteTask(IProcessor<Quote, MarketUpdate<Double, Long>> marketProcessor, Quote newQuote) {
		this.marketProcessor = marketProcessor;
		this.newQuote = newQuote;
	}

	@Override
	public MarketUpdate<Double, Long> call() throws Exception {
		MarketUpdate<Double, Long> result = marketProcessor
				.process(newQuote);
		return result;
	}
}