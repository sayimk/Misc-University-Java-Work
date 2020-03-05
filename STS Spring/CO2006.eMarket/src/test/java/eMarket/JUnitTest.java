package eMarket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eMarket.domain.Product;

public class JUnitTest {
	
	Product product;
	
@Before
public void setup() {
	// given
	product = new Product(0, "Apple", "Royal Gala", 0.24);
}

		
@Test
public void hasPrice() {
	// given
	Product p = new Product(0, "Apple", "Royal Gala", 0.24);
	
	// when
	p.setPrice(0.36);
	
	// then: 
	// 		diagnostics message,
	//		expected result, 
	//		actual result, 
	//		margin of error for comparing doubles
	Assert.assertEquals("Product price is not updated", 0.36, p.getPrice(), 0.1);
}
	
}