package com.simulator.orderbook.data;

public class Trade {

	private Integer quantity;
	private Double price;
	private String symbol;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(symbol!=null? symbol + " " :"")
		.append(getQuantity())
		.append("@")
		.append(getPrice());
		return sb.toString();
	}
	
	public boolean isEmpty(){
		return getQuantity()==null || getQuantity()<=0;
	}
}