package com.simulator.orderbook.data;


public class Quote extends Trade {
	private Side side;
	public Side getSide() {
		return side;
	}
	public void setSide(Side side) {
		this.side = side;
	}
	
}
