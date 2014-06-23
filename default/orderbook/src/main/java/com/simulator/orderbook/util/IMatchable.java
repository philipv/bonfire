package com.simulator.orderbook.util;

import com.simulator.orderbook.data.Quote;

public interface IMatchable {
	boolean isMatchable(Quote newQuote, Quote quoteOnBook);
}
