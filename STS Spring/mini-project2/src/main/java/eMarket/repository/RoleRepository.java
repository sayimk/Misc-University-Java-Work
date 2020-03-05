package eMarket.repository;

import org.springframework.data.repository.CrudRepository;

import eMarket.domain.Role;


public interface RoleRepository extends CrudRepository<Role, Integer> {
	
	public Role findById(int id);

}
