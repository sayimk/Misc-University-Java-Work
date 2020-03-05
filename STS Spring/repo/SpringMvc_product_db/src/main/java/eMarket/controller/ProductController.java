/**
 * (C) Artur Boronat, 2016
 */
package eMarket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eMarket.EMarketApp;
import eMarket.domain.Product;
import eMarket.domain.Store;
import eMarket.repository.ProductRepository;
import eMarket.repository.StoreRepository;

@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired StoreRepository storeRepo;
	@Autowired ProductRepository productRepo;

    @RequestMapping("/")
    public String index(Model model) {
    	// fetch info from repo
    	Store store = storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
    	// prepare model for view
    	model.addAttribute("productList", store.getProductList());
    	// choose view
        return "form/productMaster";
    }
    
    @RequestMapping(value = "/productDetail", method = RequestMethod.GET)
    public String productDetail(@ModelAttribute("product") Product product, @RequestParam(value="productId", required=false, defaultValue="-1") int productId) {
    	// fetch info from repo
    	Store store = storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
    	// prepare model for view
    	if (productId >= 0) {
    		// prepare for modification
    		Product p2 = store.getProductList().stream().filter(p -> (((Product) p).getId() == productId)).findAny().get();
    		product.setId(p2.getId());
    		if (p2.getName().equals("")) 
    			throw new SpringException("Name is empty.");
    		product.setName(p2.getName());
    		product.setDescription(p2.getDescription());
    		if (p2.getPrice() < 0.0) 
    			throw new SpringException("Value is negative.");
    		product.setPrice(p2.getPrice());
    	} else {
    		// prepare for addition
    	}
    	// choose view
    	return "form/productDetail";
    }   
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String productMaster(@ModelAttribute("product") Product product, Model model) {
    	// form validation
    	if (product.getPrice() < 0.0) 
			throw new SpringException("Value is negative.");
		if (product.getName().equals("")) 
			throw new SpringException("Name is empty.");    	
		// fetch model from repo
		Store store = storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
		// backend logic
		Product savedProduct = store.getProductList().stream().filter(p -> (((Product) p).getId() == product.getId())).findAny().orElse(null);
    	if (savedProduct==null) {
    		// add
    		savedProduct = product;
    	} else {
    		// update
    		savedProduct.setName(product.getName());
    		savedProduct.setDescription(product.getDescription());
    		savedProduct.setPrice(product.getPrice());
    	}
    	store.getProductList().removeIf(p -> (p.getId() == product.getId()));
    	store.getProductList().add(product);
    	storeRepo.save(store);
   		// prepare model for view
    	model.addAttribute("productList", store.getProductList());
    	// choose view
        return "form/productMaster";
//    	return "redirect:/product/";
    }   

    
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String productMaster(@RequestParam(value="productId", required=false, defaultValue="-1") int productId, Model model) {
    	// fetch info from repo
    	Store store = storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
    	// backend logic
    	store.getProductList().removeIf(p -> (p.getId() == productId));
    	storeRepo.save(store);
    	// prepare model for view
    	model.addAttribute("productList", store.getProductList());
    	// choose view
    	return "form/productMaster";
    }   
    
}
