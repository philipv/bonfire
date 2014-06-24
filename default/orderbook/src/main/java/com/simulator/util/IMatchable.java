package com.simulator.util;

import com.simulator.data.Quote;

public interface IMatchable {
	boolean isMatchable(Quote newQuote, Quote quoteOnBook);
}
