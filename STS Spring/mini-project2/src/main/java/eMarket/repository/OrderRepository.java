package eMarket.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eMarket.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {
		List<Order> findById(int id);
	    List<Order> findByUser(String user);

}

