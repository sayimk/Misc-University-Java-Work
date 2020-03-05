package eMarket.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Store {
	@Getter @Setter
	List<Product> productList = new ArrayList<>();
	
	@Getter @Setter
	List<Order> orderList = new ArrayList<>();
	
	public void init() {
		productList = new ArrayList<>();
		orderList = new ArrayList<>();
		Order.lastId=0;
		Product.lastId=0;
		OrderItem.lastId=0;
	}
}
