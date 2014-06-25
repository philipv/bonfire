package com.simulator.util;

import com.simulator.data.Quote;

public interface IMatcher {
	boolean isMatchable(Quote newQuote, Quote quoteOnBook);
}
