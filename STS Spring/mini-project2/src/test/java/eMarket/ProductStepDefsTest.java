package eMarket;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eMarket.controller.ProductController;
import eMarket.domain.Order;
import eMarket.domain.OrderItem;
import eMarket.domain.OrderStore;
import eMarket.domain.Product;
import eMarket.domain.Store;
import eMarket.repository.OrderRepository;
import eMarket.repository.OrderStoreRepository;
import eMarket.repository.ProductRepository;
import eMarket.repository.StoreRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {EMarketApp.class,WebConfig.class,DbConfig.class,ProductController.class})
@Transactional
public class ProductStepDefsTest {
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private ResultActions result;

    @Autowired 
    StoreRepository storeRepo;
    @Autowired 
    ProductRepository productRepo;
    @Autowired
    OrderStoreRepository orderStoreRepo;

    
    Product product;
    Order order;
    Store store;
    
    OrderStore orderstore;

    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
	
    @Before("@repo")
    public void beforeScenario() {
    	store = storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
    	storeRepo.delete(store);
    	
    	orderstore = orderStoreRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);
    	orderStoreRepo.delete(orderstore);
    	
    
    	if (storeRepo.findByName(EMarketApp.STORE_NAME).size() == 0) {
			store = storeRepo.save(new Store(EMarketApp.STORE_NAME));
		}
    	
    	if (orderStoreRepo.findByName(EMarketApp.ORDERSTORE_NAME).size() ==0){
    		orderstore = orderStoreRepo.save(new OrderStore(EMarketApp.ORDERSTORE_NAME));
    	}
    }

    public Store getStore() {
    	return storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
    }
    
    public OrderStore getOrderStore(){
    	return orderStoreRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);
    }

    @Given("^I have a product X with name \"([^\"]*)\", description \"([^\"]*)\" and price \"([^\"]*)\"$")
    public void i_have_a_product_X_with_name_description_and_price(String name, String description, Double price) throws Throwable {
        product = new Product(name,description,price);
    }

    @When("^I add the product using \"([^\"]*)\"$")
    public void i_add_the_product_using(String request) throws Throwable {
    	result = this.mockMvc.perform(post(request)
        	.param("id", String.valueOf(product.getId()))
    		.param("name", product.getName())
    		.param("description", product.getDescription())
        	.param("price", String.valueOf(product.getPrice())));
    }

    @Then("^a new product is stored in the repository$")
    public void a_new_product_is_stored_in_the_repository() throws Throwable {
        store = getStore();
        assertThat("Product not added to the catalogue.", store.getProductList().size(),greaterThan(0)); 
    }

    @Given("^I have a product X with name \"([^\"]*)\", description \"([^\"]*)\" and price \"([^\"]*)\" in the catalogue$")
    public void i_have_a_product_X_with_name_description_and_price_in_the_catalogue(String name, String description, Double price) throws Throwable {
    	store = getStore();
		
    	product = productRepo.save(new Product(name,description,price));
    	store.getProductList().add(product);
    	storeRepo.save(store);
    }

    @When("^I do a post \"([^\"]*)\" for the product X description \"([^\"]*)\"$")
    public void i_do_a_post_for_the_product_X_description(String request, String description) throws Throwable {
    	result = this.mockMvc.perform(post(request)
            .param("id", String.valueOf(product.getId()))
        	.param("name", product.getName())
        	.param("description", description) // <-- update
            .param("price", String.valueOf(product.getPrice())));
    }

    @Then("^the product  \"([^\"]*)\" contains the description \"([^\"]*)\"$")
    public void the_product_contains_the_description(String productName, String description) throws Throwable {
    	store = getStore();
    	Product actualProduct = store.getProductList().stream().filter(p -> p.getName().equals(productName)).findFirst().get();
        assertThat("Description attribute not updated correctly.", actualProduct.getDescription(),is(description)); 
    }

    @When("^I delete the product X using \"([^\"]*)\"$")
    public void i_delete_the_product_X_using(String request) throws Throwable {
    	result = this.mockMvc.perform(get(request)
    		.param("productId", String.valueOf(product.getId())));
    }

    @Then("^The product \"([^\"]*)\" no longer exists in the repository$")
    public void the_product_no_longer_exists_in_the_repository(String arg1) throws Throwable {
    	store = getStore();
        assertThat("The product has not been deleted.", store.getProductList().stream().filter(p -> p.getId() == product.getId()).findAny().isPresent(), is(false));
    }
    
    //OrderStep Defs
    
    @When("^I add a new order using \"([^\"]*)\"$")
    public void I_add_a_new_order_using(String request) throws Throwable {
    	result = this.mockMvc.perform(get(request));
    }
    
    @Then("^a new order is stored in the repository$")
    public void a_new_is_stored_in_the_repository(){
    	orderstore = getOrderStore();
    	assertThat(orderstore.getOrderList().size(), greaterThan(0));
    }

    @Given("^an order X without any items$")
    public void an_order_X_without_any_items() throws Throwable {
    	result = this.mockMvc.perform(get("/order/add"));
    }
    
    @When("^I delete the order X using \"([^\"]*)\"$")
    public void I_delete_the_order_X_using(String request) throws Throwable {
    	//fetch id
    	result = this.mockMvc.perform(get(request)
    			.param("orderId", "0"));

    }
    
    @Then("^The order X no longer exists in the repository$")
    public void the_order_X_no_longer_exists_in_the_repository(){
    	
    	assertThat(orderstore.getOrderList().isEmpty(),is(true));

    }
    
    
    @Given("^I have a product with name \"([^\"]*)\", description \"([^\"]*)\" and price \"([^\"]*)\" in the catalogue$")
    public void I_have_a_product_with_name_description_and_price_in_the_catalogue(String name, String description, Double price) throws Throwable{
    	store = getStore();
		
    	product = productRepo.save(new Product(name,description,price));
    	store.getProductList().add(product);
    	storeRepo.save(store);
    }
    
    
    @When("^I do a post \"([^\"]*)\" for the order X with an item with product \"([^\"]*)\" and amount \"([^\"]*)\"$")
    public void I_do_a_post_for_the_order_X_with_an_item_with_product_and_amount(String Request, String ProductName, String amount) throws Throwable{
    	
        //searching for product ID with name	
    	store = getStore();
    	
    	result = this.mockMvc.perform(post(Request)
    			.param("orderId", "1")
    			.param("productId", String.valueOf(product.getId()))
    			.param("amount", amount)
    			.param("id", "0"));
    	
    }
    
    
   @Then("^the order X contains an item with \"([^\"]*)\" products \"([^\"]*)\" and the total cost of the order is \"([^\"]*)\"$")
    public void the_order_X_contains_an_item_with_products_and_the_total_cost_of_the_order_is(int amount, String ProductName, Double totalCost){ 	
    	//doing assertThat statements
    	orderstore = getOrderStore();
    	assertThat(orderstore.getOrderList().get(0).getItemList().get(0).getAmount(),is(amount));
    	assertThat(orderstore.getOrderList().get(0).getItemList().get(0).getProductId(),is(product.getId()));
    	assertThat(orderstore.getOrderList().get(0).getItemList().get(0).getPrice(),is(totalCost));

    }
   
   
   @And("^an order X with with an item with product \"([^\"]*)\" and amount \"([^\"]*)\"$")
   public void an_order_X_with_an_item_with_product_and_amount(String ProductName, int amount) throws Throwable{
	   
	   	orderstore = getOrderStore();
	   	orderstore.getOrderList().add(new Order());
	   	orderstore.getOrderList().get(0).getItemList().add(new OrderItem(product.getId(),amount, product.getPrice()*amount));
	   	
	   	orderStoreRepo.save(orderstore);
	   
   }
   
   
   @When("^I do a get \"([^\"]*)\" for the order X and item containing \"([^\"]*)\"$")
   public void i_do_a_get_for_the_order_X_and_item_containing(String request, String ProductName ) throws Throwable{
	   
	   assertThat(orderstore.getOrderList().get(0).getItemList().get(0).getProductId(), is(product.getId()));
	   
	   String itemID = Integer.toString(orderstore.getOrderList().get(0).getItemList().get(0).getId());
	   
	   String orderID = Integer.toString(orderstore.getOrderList().get(0).getId());

	   	result = this.mockMvc.perform(get(request)
				.param("orderId", orderID)
				.param("itemId",itemID));
   }
   
   @Then("^the order X does not contain the item with \"([^\"]*)\" and the total cost of the order is \"([^\"]*)\"$")
   public void the_order_X_doe_not_contain_the_item_with_and_the_total_cost_of_the_order_is(String ProductName, Double totalCost){ 	
   	//doing assertThat statements
   	orderstore = getOrderStore();
   	assertThat(orderstore.getOrderList().get(0).getItemList().size(),is(0));
   	assertThat(orderstore.getOrderList().get(0).getCost(), is(totalCost));

   }
   

}
