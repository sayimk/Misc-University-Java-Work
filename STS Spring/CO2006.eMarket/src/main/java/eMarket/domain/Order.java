package eMarket.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import eMarket.EMarketApp;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Order {

	public static int lastId = 0;
	@Getter @Setter
    private int id = -1;
	@Getter @Setter
	private String user = "";
	@Getter @Setter
    private LocalDate date = LocalDate.now();
	@Getter @Setter
    private List<OrderItem> itemList = new ArrayList<>();
	@Getter @Setter
    private Double cost = 0.0;

	public Order() { }
	
	public String getDescription() {
		// generate a comma-separated list with the names of the products purchased
		List<String> list = itemList.stream().map(i -> i.getProduct().getName() ).collect(Collectors.toList());
		return String.join(", ", list);
	}

	// updates the id
	public void setId() {
		id=lastId;
		lastId++;
	}
	
	public void addItem(int productId, int amount) {
		Product product = getProduct(productId);
		this.getItemList().add(new OrderItem(product,amount));
		updateCost();
	}
	
	public void updateCost() {
		cost = 0.0;
		this.getItemList().forEach(i -> cost += i.getAmount() * i.getProduct().getPrice());
	}


	private Product getProduct(int productId) {
		return EMarketApp.getStore().getProductList().stream().filter(p -> p.getId()==productId).findFirst().orElse(null);
	}
}
