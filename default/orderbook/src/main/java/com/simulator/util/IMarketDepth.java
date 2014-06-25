package com.simulator.util;

import java.util.List;

import com.simulator.data.Quote;
import com.simulator.data.Trade;

/**
 * @author vinith
 * 
 * This interface is the contract for any market depth implementation. This abstracts the
 * implementation details like data structure used
 */
public interface IMarketDepth {
	
	/**
	 * This method is to accept a quote and search for the matchable opposite quotes and if any
	 * then return the trades generated from those matchable quotes and remove any existing 
	 * quotes which become empty. The criteria for matching depends on the IMatcher implementation
	 * and data structure used.
	 * @param newQuote - newly arrived quote
	 * @return trades - trades successfully matched
	 */
	public List<Trade> match(Quote newQuote);
	
	/**
	 * This method accepts a new quote and add it into the data structure. The position of the new
	 * quote is completely depndent on the data structure in use.
	 * @param newQuote
	 * @return isAdded
	 */
	boolean add(Quote newQuote);
	
	
	/**
	 * This is a matcher which decides whether a given quote on the book is matchable to the newly 
	 * arrived quote.
	 * @param matcher
	 */
	void setMatcher(IMatcher matcher);
	
}
