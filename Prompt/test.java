package com.point72.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * Implement the OrderBookImpl class such that the tests below pass.
 *
 */
public class OrderBookTest {

	@Test
	public void testBasicOrderBook() {
		OrderBook orderBook = new OrderBookImpl();

		orderBook.buy(430, 5);
		orderBook.buy(430, 5);
		orderBook.buy(430.10, 10);
		Assert.assertEquals("{bids={430.1=10, 430.0=10}, offers={}}", orderBook.getBookAsString());

		orderBook.sell(432, 2);
		orderBook.sell(433, 25);
		orderBook.sell(432.5, 8);
		orderBook.sell(433, 3);
		Assert.assertEquals("{bids={430.1=10, 430.0=10}, offers={432.0=2, 432.5=8, 433.0=28}}",
				orderBook.getBookAsString());

		orderBook.sell(420, 3);
		Assert.assertEquals("{bids={430.1=7, 430.0=10}, offers={432.0=2, 432.5=8, 433.0=28}}",
				orderBook.getBookAsString());

		orderBook.buy(500, 100);
		Assert.assertEquals("{bids={500.0=62, 430.1=7, 430.0=10}, offers={}}", orderBook.getBookAsString());
	}

}
