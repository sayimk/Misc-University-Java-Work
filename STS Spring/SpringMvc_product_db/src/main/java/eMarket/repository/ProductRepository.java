package eMarket.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eMarket.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
	List<Product> findById(int id);
    List<Product> findByName(String name);
}