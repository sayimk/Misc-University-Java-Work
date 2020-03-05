package eMarket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import eMarket.domain.OrderStore;
import eMarket.domain.Role;
import eMarket.domain.Store;
import eMarket.domain.User;
import eMarket.repository.OrderStoreRepository;
import eMarket.repository.StoreRepository;
import eMarket.repository.UserRepository;

@SpringBootApplication
public class EMarketApp implements CommandLineRunner { 

	@Autowired 
	StoreRepository repo;
	
	@Autowired
	OrderStoreRepository orderrepo;
	
	@Autowired
	private UserRepository userRepo;	

	public static final String STORE_NAME = "MyEMarket";
	public static final String ORDERSTORE_NAME = "MyOrders";
	
	public static final int ROLE_MANAGER = 1;
	public static final int ROLE_CUSTOMER = 2;

	
    public static void main(String[] args) {
        SpringApplication.run(EMarketApp.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
		// creation of instance according to the object diagram in Fig. 2
		
		if (repo.findByName(STORE_NAME).size() == 0) {
			Store store = new Store(STORE_NAME);
			repo.save(store);
		}
		
		if (orderrepo.findByName(ORDERSTORE_NAME).size() == 0) {
			OrderStore orderstore = new OrderStore(ORDERSTORE_NAME);
			orderrepo.save(orderstore);
		}
		
		BCryptPasswordEncoder pe = new  BCryptPasswordEncoder();

		//creating customer user
		User user = new User();
		user.setLogin("Alice");
		user.setPassword(pe.encode("password"));
		Role role = new Role();
		role.setId(ROLE_CUSTOMER);
		role.setRole("ROLE_CUSTOMER");
		user.setRole(role);
		userRepo.save(user);
		
		
		//creating Manager User
		User user1 = new User();
		user1.setLogin("Bob");
		user1.setPassword(pe.encode("admin"));
		Role role1 = new Role();
		role1.setId(ROLE_MANAGER);
		role1.setRole("ROLE_MANAGER");
		user1.setRole(role1);
		userRepo.save(user1);
		
		
    }   

}
