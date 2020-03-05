package eMarket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import eMarket.controller.ItemController;
import eMarket.controller.OrderController;
import eMarket.controller.ProductController;
import eMarket.domain.Order;
import eMarket.domain.OrderItem;
import eMarket.domain.Product;

@WebAppConfiguration
@ContextConfiguration(classes = {ProductController.class, OrderController.class, ItemController.class})
public class StepDefs {
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private ResultActions result;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
	
    @Before("@logic")
    public void beforeScenario() {
    	EMarketApp.getStore().init();
    }

    @When("^I do a post \"([^\"]*)\" with id \"([^\"]*)\", name \"([^\"]*)\", description \"([^\"]*)\" and price \"([^\"]*)\"$")
    public void i_do_a_post_with_id_name_description_and_price(String request, String id, String name, String description, String price) throws Throwable {
    	result = this.mockMvc.perform(post(request)
    			.param("id", id)
    			.param("name", name)
    			.param("description", description)
    			.param("price", price));
    }

    @When("^I do a get \"([^\"]*)\"$")
    public void i_get(String arg1) throws Throwable {
		result = this.mockMvc.perform(get(arg1));
	}
    
    @Then("^I should see this view \"([^\"]*)\"$")
    public void i_should_see_this_view(String arg1) throws Throwable {
		result
		.andExpect(status().isOk())
		.andExpect(view().name(arg1));
    }    

    
    Product product;
    @Given("^I have a product with id \"([^\"]*)\", name \"([^\"]*)\", description \"([^\"]*)\" and price \"([^\"]*)\" in the catalogue$")
    public void i_have_a_product_with_id_name_description_and_price(int id, String name, String description, Double price) throws Throwable {
    	product = new Product(id,name,description,price);
    	EMarketApp.getStore().getProductList().add(product);
    }

    @When("^I edit description to \"([^\"]*)\" using \"([^\"]*)\"$")
    public void i_edit_description_to(String description, String request) throws Throwable {
        	result = this.mockMvc.perform(post(request)
        		.param("id", String.valueOf(product.getId()))
        		.param("name", product.getName())
        		.param("description", description)
        		.param("price", product.getPrice().toString()));
    }
    
    @Then("^the description should be \"([^\"]*)\" for product with id \"([^\"]*)\" in the catalogue$")
    public void the_description_should_be_for_product_with_id(String description, int productId) throws Throwable {
    	Product p2 = EMarketApp.getStore().getProductList().stream().filter(p -> (((Product) p).getId() == productId)).findAny().get();
    	assertThat(p2.getDescription(), is(description));
    }    
    
    boolean caughtException;
    @When("^I add a product with id \"([^\"]*)\", name \"([^\"]*)\", description \"([^\"]*)\" and price \"([^\"]*)\" using \"([^\"]*)\"$")
    public void i_add_a_product_with_id_name_description_and_price(String id, String name, String description, String price, String request) throws Throwable {
    	caughtException = false;
    	try {
	        result = this.mockMvc.perform(post(request)
	        		.param("id", id)
	        		.param("name", name)
	        		.param("description", description)
	        		.param("price", price));
    	} catch (Exception e) {
    		caughtException = true;
    	}
    }
    

	@Then("^I should get an exception$")
	public void i_should_get_an_exception() throws Throwable {
		assertThat(caughtException,is(true));
	}

    @Then("^the product stored with id \"([^\"]*)\" corresponds to name \"([^\"]*)\", description \"([^\"]*)\" and price \"([^\"]*)\"$")
    public void the_product_stored_with_id_corresponds_to_name_description_and_price(int id, String name, String description, Double price) throws Throwable {
        Product p2 = EMarketApp.getStore().getProductList().stream().filter(p -> (((Product) p).getId() == id)).findAny().get();
        assertThat(p2.getName(), is(name));
    	assertThat(p2.getDescription(), is(description));
    	assertThat(p2.getPrice(), is(price));
    }

    @When("^I delete the product with id \"([^\"]*)\" using \"([^\"]*)\"$")
    public void i_delete_the_product_with_id(String id, String request) throws Throwable {
    	result = this.mockMvc.perform(get(request)
    		.param("productId", id));
    }

    @Then("^the product with id \"([^\"]*)\" no longer exists in the catalogue$")
    public void the_product_with_id_no_longer_exists(int id) throws Throwable {
    	assertThat(EMarketApp.getStore().getProductList().stream().filter(p -> (((Product) p).getId() == id)).findAny().isPresent(), is(false));
    }
    
    
    
    // NEEDED FOR THE MINI-PROJECT
    
    
    // order
    @Given("^an order with id \"([^\"]*)\"$")
    public void an_order_with_id(String arg1) throws Throwable {
    	Order o = new Order();
    	o.setId(0);
    	EMarketApp.getStore().getOrderList().add(o);
    }
    
    

    @When("^I do a post \"([^\"]*)\" for the order with id \"([^\"]*)\" with an item with id \"([^\"]*)\" with product id \"([^\"]*)\" and amount \"([^\"]*)\"$")
    public void i_do_a_post_for_the_order_with_id_with_an_item_with_id_with_product_id_and_amount(String request, String orderId, String itemId, String productId, String amount) throws Throwable {
    	caughtException = false;
        try {

	        result = this.mockMvc.perform(post(request)
	        		.param("action", "Submit")
	        		.param("id", itemId)
	        		.param("orderId", orderId)
	        		.param("productId", productId)
	        		.param("amount", amount));
        
        } catch (Exception e) {
        	caughtException = true;

        }
    }    

    
    // logic Order
    @When("^I add a new order using \"([^\"]*)\"$")
	public void i_add_a_new_order(String request) throws Throwable {
    	result = this.mockMvc.perform(get(request));
	}
	
	@Then("^a new order is stored in the system$")
	public void a_new_order_is_stored_in_the_system() throws Throwable {
		assertThat(EMarketApp.getStore().getOrderList().size(), is(1));
	}
	
	@Given("^I have an order with id \"([^\"]*)\" in the catalogue$")
	public void i_have_an_order_with_id_in_the_catalogue(String arg1) throws Throwable {
		Order o = new Order();
		o.setId(0);
	    EMarketApp.getStore().getOrderList().add(o);
	}
	
	@When("^I delete the order with id \"([^\"]*)\" using \"([^\"]*)\"$")
	public void i_delete_the_order_with_id_using(String orderId, String request) throws Throwable {
	    result = this.mockMvc.perform(get(request)
	    		.param("orderId", orderId));
	}
	
	@Then("^The order with id \"([^\"]*)\" no longer exists in the catalogue$")
	public void the_order_with_id_no_longer_exists_in_the_catalogue(String arg1) throws Throwable {
	    assertThat(EMarketApp.getStore().getOrderList().size(), is(0));
	}
	

    // logic Item

    @Then("^the order with id \"([^\"]*)\" contains an item with id \"([^\"]*)\", \"([^\"]*)\" products of id \"([^\"]*)\" and the total cost of the order is \"([^\"]*)\"$")
	public void the_order_with_id_contains_an_item_with_id_products_of_id_and_the_total_cost_of_the_orderis(int orderId, int itemId, int amount, int productId, Double cost) throws Throwable {
		Order o2 = EMarketApp.getStore().getOrderList().stream().filter(o -> (((Order) o).getId() == orderId)).findAny().get();
		assertThat(o2.getCost(), is(cost));
		OrderItem i2 = o2.getItemList().stream().filter(i -> i.getId() == itemId).findAny().get();
		assertThat(i2.getProduct().getId(), is(itemId));
		assertThat(i2.getAmount(), is(amount));
	}


	@Given("^an order with id \"([^\"]*)\" with an item with id \"([^\"]*)\", amount \"([^\"]*)\" and product id \"([^\"]*)\"$")
	public void an_order_with_id_with_an_item_with_id_amount_and_product_id(int orderId, int itemId, int amount, int productId) throws Throwable {
		Order o = new Order();
		o.setId(orderId);
		o.addItem(productId, amount);
	    EMarketApp.getStore().getOrderList().add(o);
	}


	@When("^I do a get \"([^\"]*)\" for the order with id \"([^\"]*)\" and item with id \"([^\"]*)\"$")
	public void i_do_a_get_for_the_order_with_id_and_item_with_id(String request, String orderId, String itemId) throws Throwable {
		result = this.mockMvc.perform(get(request)
        		.param("itemId", itemId)
        		.param("orderId", orderId));
	}
	
	@Then("^the order with id \"([^\"]*)\" does not contain item with id \"([^\"]*)\" and the total cost of the order is \"([^\"]*)\"$")
	public void the_order_with_id_does_not_contain_item_with_id_and_the_total_cost_of_the_order_is(int orderId, int itemId, Double cost) throws Throwable {
		Order o2 = EMarketApp.getStore().getOrderList().stream().filter(o -> (((Order) o).getId() == orderId)).findAny().get();
		assertThat(o2.getCost(), is(cost));
		assertThat(o2.getItemList().stream().filter(i -> i.getId() == itemId).findAny().isPresent(),is(false));
	}
    
}
