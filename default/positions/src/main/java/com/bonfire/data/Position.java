package com.bonfire.data;

/*
 * Class to represent a position update
 */
public class Position {
	private String currency;
	private Double value;
	
	public Position(){}
	
	public Position(String currency, Double value){
		setCurrency(currency);
		setValue(value);
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
}
