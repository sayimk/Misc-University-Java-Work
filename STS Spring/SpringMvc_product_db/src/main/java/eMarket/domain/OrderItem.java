package eMarket.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderItem {

	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column
    private int id;
	
	@Column
    private int productId;
	@Column
    private int amount;
	@Column
    private Double cost;
    
    public OrderItem(int productId, int amount, Double Price){
    	this.productId = productId;
    	this.amount = amount;
    	this.cost = amount * Price;
    }
    
    public OrderItem(){
    	
    }
    
    public int getId(){
    	return id;
    }
    
    public int getProductId(){
    	return productId;
    }
    
    public int getAmount(){
    	return amount;
    }
    
    public Double getPrice(){
    	return cost;
    }
    
    public void setProductId(int productId){
    	this.productId=productId;
    }
    
    
    public void setId(int id){
    	this.id=id;
    }
    
    public void setAmount(int Amount){
    	this.amount=Amount;
    }
    
    public void setPrice(Double Price){
    	cost=Price;
    }
    
    public void overWrite(int productId, int amount, Double Price){
    	this.productId = productId;
    	this.amount = amount;
    	this.cost = amount * Price;
    }
}
