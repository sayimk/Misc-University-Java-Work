package eMarket.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class OrderItem {

	public static int lastId = 0;
	@Getter @Setter
    private int id = -1;
	@Getter @Setter
    private Product product;
	@Getter @Setter
    private int amount;
	@Getter @Setter
    private Double cost;
    
    public OrderItem(Product product, int amount){
    	this.id = lastId;
    	lastId++;
    	this.product = product;
    	this.amount = amount;
    	this.cost = amount * product.getPrice();
    }
    
}
