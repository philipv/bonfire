package com.simulator.data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MarketUpdate<K,V> extends MarketIdentity{
	private static final String LINE_SEPARATOR = "----------------------------------------------\n";
	private static final int PADDING = 20;
	private List<Trade> trades;
	private Map<K, V> bidUpdates;
	private Map<K, V> askUpdates;
	public MarketUpdate(List<Trade> trades) {
		super();
		this.trades = trades;
		this.bidUpdates = new HashMap<K, V>();
		this.askUpdates = new HashMap<K, V>();
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
	public void addBidUpdates(K key, V value) {
		bidUpdates.put(key, value);
	}
	public Map<K, V> getAskUpdates() {
		return askUpdates;
	}
	public void addAskUpdates(K key, V value) {
		askUpdates.put(key, value);
	}
	public void addAskUpdates(Map<K, V> askUpdates) {
		this.askUpdates = new HashMap<>(askUpdates);
	}
	public void addBidUpdates(Map<K, V> bidUpdates) {
		this.bidUpdates = new HashMap<>(bidUpdates);
		
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getSymbol()==null?"":getSymbol()).append("\n");
		if(trades!=null && !trades.isEmpty()){
			sb.append("Trades\n").append(LINE_SEPARATOR);
			for(Trade trade:trades){
				sb.append(trade).append("\n");
			}
		}
		if(!bidUpdates.isEmpty() || !askUpdates.isEmpty()){
			Iterator<Entry<K, V>> sortedBidMapIterator = new TreeMap<>(bidUpdates).descendingMap().entrySet().iterator();
			Iterator<Entry<K, V>> sortedAskMapIterator = new TreeMap<>(askUpdates).entrySet().iterator();
			sb.append(String.format("%1$-" + PADDING + "s", "BUY")).append("|");
			sb.append(String.format("%1$" + PADDING + "s", "SELL")).append("\n");
			sb.append(LINE_SEPARATOR);
			for(int i=0; i<Math.max(askUpdates.size(), bidUpdates.size()); i++){
				if(sortedBidMapIterator.hasNext()){
					Entry<K, V> sortedBidMapEntry = sortedBidMapIterator.next();
					sb.append(String.format("%1$-" + PADDING + "s", sortedBidMapEntry.getValue() + "@" + sortedBidMapEntry.getKey())).append("|");
				}else{
					sb.append(String.format("%1$-" + PADDING + "s", "")).append("|");
				}
				if(sortedAskMapIterator.hasNext()){
					Entry<K, V> sortedAskMapEntry = sortedAskMapIterator.next();
					sb.append(String.format("%1$" + PADDING + "s", sortedAskMapEntry.getValue() + "@" + sortedAskMapEntry.getKey())).append("\n");
				}else{
					sb.append(String.format("%1$" + PADDING + "s", "")).append("\n");
				}
			}
		}
		return sb.toString();
	}
}
