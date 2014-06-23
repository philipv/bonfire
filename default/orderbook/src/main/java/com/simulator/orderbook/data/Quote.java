package com.simulator.orderbook.data;


public class Quote extends Trade implements Comparable<Quote>{
	private Side side;
	
	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}
	
	@Override
	public int compareTo(Quote o) {
		return getPrice().compareTo(o.getPrice());
	}
}
