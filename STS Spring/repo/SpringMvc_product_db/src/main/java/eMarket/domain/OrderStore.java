package eMarket.domain;

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


@Entity 
public class OrderStore {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name="orderstore_id")
	private int id;

	@Column
	private String name;
	
	// CascadeType.REMOVE included in CascadeType.ALL cascades removal when Store object is removed
	// orphan removal forces the deletion of the object when the reference is removed
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	List<Order> OrderList = new ArrayList<>();
	
	public OrderStore() {
		
	}
	
	public OrderStore(String name) {
		this.name = name;
	}
	
	public void setOrderList(List<Order> OrderList) {
		this.OrderList = OrderList;
	}
	
	public List<Order> getOrderList() {
		return OrderList;
	}
	
}
