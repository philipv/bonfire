package com.simulator.util;

import java.util.List;

import com.simulator.data.Quote;
import com.simulator.data.Trade;

public interface MarketDepth {
	public List<Trade> match(Quote newQuote);

	boolean add(Quote newQuote);
}
