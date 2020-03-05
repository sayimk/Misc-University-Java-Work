package eMarket.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import eMarket.domain.Store;

public interface StoreRepository extends CrudRepository<Store, Integer> {
    List<Store> findByName(String name);
}