package com.simulator.orderbook.data;

import java.math.BigDecimal;
import java.util.List;

public class PriceLevel {
	private BigDecimal price;
	private List<Quote> quotes;
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public List<Quote> getQuotes() {
		return quotes;
	}
	public void setQuotes(List<Quote> quotes) {
		this.quotes = quotes;
	}
	
	

}
