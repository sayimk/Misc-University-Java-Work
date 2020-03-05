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
import org.springframework.beans.factory.annotation.Autowired;

import eMarket.EMarketApp;
import eMarket.domain.Order;
import eMarket.domain.OrderStore;
import eMarket.repository.OrderStoreRepository;


@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired OrderStoreRepository OrderRepo;
	
    @RequestMapping("/")
    public String orderMaster(Model model) {
    	OrderStore orderstore = OrderRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);

    	model.addAttribute("orderList", orderstore.getOrderList());
        return "form/orderMaster";
    }
   
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String orderDetail(@ModelAttribute("order") Order order, @RequestParam(value="orderId", required=false, defaultValue="-1") int orderId) {
    	if (orderId > -1) {
    		
    		//fetching orderList
        	OrderStore orderstore = OrderRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);        		
    	
        	
	    	Order orderAux = orderstore.getOrderList().stream().filter(o -> o.getId()==orderId).findFirst().orElse(null);
	    	order.setId(orderAux.getId());
	    	order.setDate(orderAux.getDate());
	    	order.setItemList(orderAux.getItemList()); //do without items list
	    	order.setUser(orderAux.getUser());
	    	order.setCost(orderAux.getCost());
	    	System.out.println(order.toString());
    	} else {
    		// new instance
    		Order orderAux=order;
        	OrderStore orderstore = OrderRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);        		
    		orderstore.getOrderList().add(orderAux);
    		OrderRepo.save(orderstore);
    	}
    	return "form/orderDetail";
    }   

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam(value="orderId", required=false, defaultValue="-1") int orderId, Model model) {
    	OrderStore orderstore = OrderRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);
    	
    	orderstore.getOrderList().removeIf(p -> (p.getId() == orderId));
    	model.addAttribute("orderList", orderstore.getOrderList());
		OrderRepo.save(orderstore);

    	return "form/orderMaster";
    }   
   

    
}
