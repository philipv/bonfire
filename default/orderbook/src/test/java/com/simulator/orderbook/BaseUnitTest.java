package com.simulator.orderbook;

import com.simulator.orderbook.data.Quote;
import com.simulator.orderbook.data.Side;


public class BaseUnitTest {
	protected Quote createQuote(double price, int quantity){
		Quote quote = new Quote();
		quote.setPrice(price);
		quote.setQuantity(quantity);
		return quote;
	}
	
	protected Quote createQuote(double price, int quantity, Side side){
		Quote quote = createQuote(price, quantity);
		quote.setSide(side);
		return quote;
	}
}
