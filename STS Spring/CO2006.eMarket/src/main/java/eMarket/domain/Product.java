package eMarket.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Product {

	public static int lastId = 0;
	@Getter @Setter
    private int id = -1;
	@Getter @Setter
    private String name;
	@Getter @Setter
    private String description;
	@Getter @Setter
    private Double price;
    
    public Product(){}
    
    public Product(int id, String name, String description, Double price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
	}

	public void setId() {
		this.id = lastId;
		lastId++;
	}

}
