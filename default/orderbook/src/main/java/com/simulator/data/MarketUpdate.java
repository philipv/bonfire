package com.simulator.data;

import java.util.List;
import java.util.Map;

public class MarketUpdate<K,V> {
	
	private List<Trade> trades;
	private Map<K, V> bidUpdates;
	private Map<K, V> askUpdates;
	public MarketUpdate(List<Trade> trades, Map<K, V> bidUpdates,
			Map<K, V> askUpdates) {
		super();
		this.trades = trades;
		this.bidUpdates = bidUpdates;
		this.askUpdates = askUpdates;
	}
	public List<Trade> getTrades() {
		return trades;
	}
	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}
	public Map<K, V> getBidUpdates() {
		return bidUpdates;
	}
	public void setBidUpdates(Map<K, V> bidUpdates) {
		this.bidUpdates = bidUpdates;
	}
	public Map<K, V> getAskUpdates() {
		return askUpdates;
	}
	public void setAskUpdates(Map<K, V> askUpdates) {
		this.askUpdates = askUpdates;
	}
}
