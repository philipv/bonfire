package com.simulator.orderbook.data;

public class Trade {

	private Integer quantity;
	private Double price;

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getQuantity())
		.append("@")
		.append(getPrice());
		return sb.toString();
	}
	
	

}