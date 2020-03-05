/**
 * (C) Artur Boronat, 2015
 */
package eMarket.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eMarket.EMarketApp;
import eMarket.domain.Order;
import eMarket.domain.OrderItem;
import eMarket.domain.Product;

@Controller
@RequestMapping("/item")
public class ItemController {
	
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String itemDetail(
    		@ModelAttribute("itemFormDto") ItemFormDto itemFormDto, 
    		@RequestParam(value="itemId", required=false, defaultValue="-1") int itemId, 
    		@RequestParam(value="orderId", required=true, defaultValue="-1") int orderId
    ) {
    	if (itemId > -1) {
    		Order order = EMarketApp.getStore().getOrderList().stream().filter(o -> o.getId() == orderId).findFirst().orElse(null);
	    	OrderItem item = order.getItemList().stream().filter(p -> p.getId()==itemId).findFirst().orElse(null);
	    	itemFormDto.setId(itemId);
	    	itemFormDto.setProductId(item.getProduct().getId());
	    	itemFormDto.setAmount(item.getAmount());
    	}
    	itemFormDto.setOrderId(orderId);
    	itemFormDto.setProductList(EMarketApp.getStore().getProductList());
    	return "form/itemDetail";
    }   
    
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ExceptionHandler(SpringException.class)
    public String add(@ModelAttribute("itemFormDto") ItemFormDto itemFormDto, @RequestParam(value="action") String action,  Model model) {
    	if (itemFormDto.getAmount() < 0) 
			throw new SpringException("Amount is negative.");
    	
    	
    	Order order = EMarketApp.getStore().getOrderList().stream().filter(o -> o.getId() == itemFormDto.getOrderId()).findFirst().orElse(null);
    	if (action.startsWith("Submit")) {
    		Optional<OrderItem> itemOp = order.getItemList().stream().filter(p -> (p.getId() == itemFormDto.getId())).findFirst();
    		if (itemOp.isPresent()) {
    			// edit
    			OrderItem item = itemOp.get();
    			Product product = EMarketApp.getStore().getProductList().stream().filter(p -> p.getId() == itemFormDto.getProductId()).findFirst().get();
    			item.setProduct(product);
    			item.setAmount(itemFormDto.getAmount());
    			item.setCost(item.getProduct().getPrice() * item.getAmount());
    			order.updateCost();
    			
    		} else {
    			// create
    			order.addItem(itemFormDto.getProductId(), itemFormDto.getAmount());
    		}
    	} 
    	model.addAttribute("order", order);
    	return "form/orderDetail";
    }   

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(
    		@RequestParam(value="itemId", required=false, defaultValue="-1") int itemId, 
    		@RequestParam(value="orderId", required=true, defaultValue="-1") int orderId,
    		Model model
    ) {
    	Order order = EMarketApp.getStore().getOrderList().stream().filter(o -> o.getId()==orderId).findFirst().get();
    	order.getItemList().removeIf(p -> p.getId()==itemId);
    	order.updateCost();
    	model.addAttribute("order", order);
    	return "form/orderDetail";
    }   
    


    
}
