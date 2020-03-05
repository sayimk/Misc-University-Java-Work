/**
 * (C) Artur Boronat, 2015
 */
package eMarket.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import eMarket.domain.OrderStore;
import eMarket.domain.Product;
import eMarket.domain.Store;
import eMarket.repository.OrderStoreRepository;
import eMarket.repository.StoreRepository;

@Controller
@RequestMapping("/item")
public class ItemController {
	@Autowired OrderStoreRepository OrderRepo;
	@Autowired StoreRepository storeRepo;

	
    @RequestMapping(value = "/detail", method = RequestMethod.GET) //double check
    public String itemDetail(
    		@ModelAttribute("itemFormDto") ItemFormDto itemFormDto, 
    		@RequestParam(value="itemId", required=false, defaultValue="-1") int itemId, 
    		@RequestParam(value="orderId", required=true, defaultValue="-1") int orderId,
    		Model model
    ) {
    	Store store = storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
    	OrderStore orderstore = OrderRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);        		
    	if (itemId > -1) {

    		
    		Order order = orderstore.getOrderList().stream().filter(o -> o.getId() == orderId).findFirst().orElse(null);
	    	OrderItem item = order.getItemList().stream().filter(p -> p.getId()==itemId).findFirst().orElse(null);
	    	itemFormDto.setId(itemId);
	    	itemFormDto.setProductId(item.getProductId());
	    	itemFormDto.setAmount(item.getAmount());
    	}
    	model.addAttribute("requestOrderId", orderId);
    	itemFormDto.setOrderId(orderId);
    	itemFormDto.setProductList(store.getProductList());
    	return "form/itemDetail";
    	
    }   
    
       @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ExceptionHandler(SpringException.class)
    public String add(HttpServletRequest request, Model model) {
    	int requestOrderId =(Integer.parseInt(request.getParameter("orderId"))-1);  //check order number
    	int requestProductId = (Integer.parseInt(request.getParameter("productId")));
    	int requestAmount = (Integer.parseInt(request.getParameter("amount")));
    	int requestid= (Integer.parseInt(request.getParameter("id")));
    	
    	Store store = storeRepo.findByName(EMarketApp.STORE_NAME).get(0);
    	OrderStore orderstore = OrderRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);        
		Product savedProduct = store.getProductList().stream().filter(p -> (((Product) p).getId() == requestProductId)).findAny().orElse(null);

	 	 if (requestAmount < 0) 
				throw new SpringException("Amount is negative.");
	 	 
	    Order order = orderstore.getOrderList().get(requestOrderId); 
	    
	    if (order.getItemList().stream().filter(p -> (p.getId() == requestid)).findAny().isPresent()){
	    	orderstore.getOrderList().get(requestOrderId).getItemList().get(requestid-1).overWrite(requestProductId, requestAmount, savedProduct.getPrice());
	    	
	    }else{
	    	
	    	orderstore.getOrderList().get(requestOrderId).addItem(requestProductId, requestAmount,savedProduct.getPrice());
	   }
    	OrderRepo.save(orderstore);

    	model.addAttribute("orderList", orderstore.getOrderList());

    	return "form/orderMaster";


    }
    


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(
    		@RequestParam(value="itemId", required=false, defaultValue="-1") int itemId, 
    		@RequestParam(value="orderId", required=true, defaultValue="-1") int orderId,
    		Model model
    ) {
    	OrderStore orderstore = OrderRepo.findByName(EMarketApp.ORDERSTORE_NAME).get(0);        		
    	Order order = orderstore.getOrderList().stream().filter(o -> o.getId()==orderId).findFirst().get();
    	order.getItemList().removeIf(p -> p.getId()==itemId);
		
    	//manually adding up total
		Double cost=0.0;
		//manually add up all orderItems Prices    	use getItemList.get(i).getPrice and add to cost	    			
		for(int i=0; i<order.getItemList().size();i++){
			cost = cost+order.getItemList().get(i).getPrice();
		}
		order.setCost(cost);
    	model.addAttribute("order", order);
    	
    	orderstore.getOrderList().removeIf(p -> (p.getId() == order.getId()));
    	orderstore.getOrderList().add(order);
    	OrderRepo.save(orderstore);
    	return "form/orderDetail";
    }   
    


    
}
