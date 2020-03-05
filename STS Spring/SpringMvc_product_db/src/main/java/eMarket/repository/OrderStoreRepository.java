package eMarket.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eMarket.domain.OrderStore;

public interface OrderStoreRepository extends CrudRepository<OrderStore, Integer> {
	   List<OrderStore> findByName(String name);

}
