/**
 * (C) Artur Boronat, 2015
 */
package eMarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eMarket.EMarketApp;
import eMarket.domain.Product;

@Controller
@RequestMapping("/product")
public class ProductController {

    @RequestMapping("/")
    public String index(Model model) {
    	model.addAttribute("productList", EMarketApp.getStore().getProductList());
        return "form/productMaster";
    }
    
    @RequestMapping(value = "/productDetail", method = RequestMethod.GET)
    @ExceptionHandler({SpringException.class})
    public String productDetail(@ModelAttribute("product") Product product, @RequestParam(value="productId", required=false, defaultValue="-1") int productId) {
    	if (productId >= 0) {
    		// modify
    		Product p2 = EMarketApp.getStore().getProductList().stream().filter(p -> (((Product) p).getId() == productId)).findAny().get();
    		product.setId(p2.getId());
    		if (p2.getName().equals("")) 
    			throw new SpringException("Name is empty.");
    		product.setName(p2.getName());
    		product.setDescription(p2.getDescription());
    		if (p2.getPrice() < 0.0) 
    			throw new SpringException("Value is negative.");
    		product.setPrice(p2.getPrice());
    	} else {
    		// add
    		product.setId();
    	}
    	return "form/productDetail";
    }   
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String productMaster(@ModelAttribute("product") Product product, Model model) {
    	if (product.getPrice() < 0.0) 
			throw new SpringException("Value is negative.");
		if (product.getName().equals("")) 
			throw new SpringException("Name is empty.");    	

    	EMarketApp.getStore().getProductList().removeIf(p -> (p.getId() == product.getId()));
    	EMarketApp.getStore().getProductList().add(product);
   		
    	model.addAttribute("productList", EMarketApp.getStore().getProductList());
        return "form/productMaster";
//    	return "redirect:/product/";
    }   

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String productMaster(@RequestParam(value="productId", required=false, defaultValue="-1") int productId, Model model) {
    	EMarketApp.getStore().getProductList().removeIf(p -> (p.getId() == productId));
    	model.addAttribute("productList", EMarketApp.getStore().getProductList());
    	return "form/productMaster";
    }   
    
    
    
}
