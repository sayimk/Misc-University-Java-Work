package eMarket.domain;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

//@ToString
@Entity(name="orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;
	
	@Column
	private String user;
	
	@Column
	private LocalDate date = LocalDate.now();
	
	//add column for order items
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	List<OrderItem> itemList = new ArrayList<>();
	
	@Column
	private Double cost = 0.0;
	
	
	@Column
	private int lastId = 0;	

	public Order() {
	}
	
	/*public String getDescription() {
		// generate a comma-separated list with the names of the products purchased
		List<String> list = itemList.stream().map(i -> i.getProduct().getName() ).collect(Collectors.toList());
		return String.join(", ", list);
	}*/

	// updates the id

	
	public void setId(int id){ 
		this.id=id;
	}
	
	public void setItemList(List<OrderItem> itemList){
		this.itemList=itemList;
	}
	
	public List<OrderItem> getItemList(){
		return itemList;
	}
	
	public int getId(){
		return id;
	}
	
	public LocalDate getDate(){
		return date;
	}
	
	
	public void setDate(LocalDate date){
		this.date = date;
	}
	
	public String getUser(){
		return user;
	}
	
	public void setUser(String user){
		this.user = user;
	}
	
	public Double getCost(){
		DecimalFormat df = new DecimalFormat("#.##");
		cost=0.0;
		for (int i=0; i< itemList.size();i++){
			cost=cost+itemList.get(i).getPrice();
		}
		
		return Double.parseDouble(df.format(cost));
	}
	
	public void setCost(Double cost){
		this.cost = cost;
	}


	
	public void addItem(int productid, int amount, double cost) {
		itemList.add(new OrderItem(productid,amount,cost));
	}


	/*private Product getProduct(int productId) {
    	Store store = storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
		return store.getProductList().stream().filter(p -> p.getId()==productId).findFirst().orElse(null);
	}*/
}
