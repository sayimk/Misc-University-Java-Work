/**
 * (C) Artur Boronat, 2015
 */
package eMarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import eMarket.EMarketApp;
import eMarket.domain.Order;

@Controller
@RequestMapping("/order")
public class OrderController {
	
    @RequestMapping("/")
    public String orderMaster(Model model) {
    	model.addAttribute("orderList", EMarketApp.getStore().getOrderList());
        return "form/orderMaster";
    }
   
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String orderDetail(@ModelAttribute("order") Order order, @RequestParam(value="orderId", required=false, defaultValue="-1") int orderId) {
    	if (orderId > -1) {
	    	Order orderAux = EMarketApp.getStore().getOrderList().stream().filter(o -> o.getId()==orderId).findFirst().orElse(null);
	    	order.setId(orderAux.getId());
	    	order.setDate(orderAux.getDate());
	    	order.setItemList(orderAux.getItemList());
	    	order.setUser(orderAux.getUser());
	    	order.setCost(orderAux.getCost());
	    	System.out.println(order.toString());
    	} else {
    		// new instance
    		order.setId();
    		EMarketApp.getStore().getOrderList().add(order);
    	}
    	return "form/orderDetail";
    }   

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value="orderId", required=false, defaultValue="-1") int orderId, Model model) {
    	EMarketApp.getStore().getOrderList().removeIf(p -> (p.getId() == orderId));
    	model.addAttribute("orderList", EMarketApp.getStore().getOrderList());
    	return "form/orderMaster";
    }   
   

    
}
