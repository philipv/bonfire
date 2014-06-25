package com.simulator.data;

public class Trade extends MarketIdentity {

	private Long quantity;
	private Double price;
	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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