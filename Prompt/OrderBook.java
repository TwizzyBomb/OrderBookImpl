package com.point72.test;

public interface OrderBook {

	void buy(double limitPrice, int quantity);

	void sell(double limitPrice, int quantity);

	String getBookAsString();

}