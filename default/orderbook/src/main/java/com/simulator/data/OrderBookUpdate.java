package com.simulator.data;

import java.util.List;
import java.util.Map;

public class OrderBookUpdate<K,V> {
	
	private List<Trade> trades;
	private Map<K, V> bidUpdates;
	private Map<K, V> askUpdates;
	public OrderBookUpdate(List<Trade> trades, Map<K, V> bidUpdates,
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
	public void addBidUpdate(K key, V value){
		bidUpdates.put(key, value);
	}
	public Map<K, V> getAskUpdates() {
		return askUpdates;
	}
	public void setAskUpdates(Map<K, V> askUpdates) {
		this.askUpdates = askUpdates;
	}
	public void addAskUpdate(K key, V value){
		askUpdates.put(key, value);
	}
	
	

}
