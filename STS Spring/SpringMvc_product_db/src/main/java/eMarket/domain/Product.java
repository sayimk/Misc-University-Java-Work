/**
 * (C) Artur Boronat, 2016
 */
package eMarket.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
	@Column
    private String name;
	@Column
	private String description;
	@Column
	private Double price;
    
    public Product(){}
    
    public Product(String name, String description, Double price) {
		this.setName(name);
		this.setDescription(description);
		this.setPrice(price);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}


}
