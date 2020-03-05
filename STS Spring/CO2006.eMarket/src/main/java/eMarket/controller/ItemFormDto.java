package eMarket.controller;

import java.util.List;

import eMarket.domain.Product;
import lombok.Getter;
import lombok.Setter;

public class ItemFormDto {
	@Getter @Setter
	private int orderId=-1;
	@Getter @Setter
	private int id=-1;
	@Getter @Setter
	private List<Product> productList;
	@Getter @Setter
	private int productId = -1;
	@Getter @Setter
	private int amount = 0;
    
}
