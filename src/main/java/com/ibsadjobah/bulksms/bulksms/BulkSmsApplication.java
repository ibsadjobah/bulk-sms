package com.ibsadjobah.bulksms.bulksms;

import com.ibsadjobah.bulksms.bulksms.model.entities.Customer;
import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
import com.ibsadjobah.bulksms.bulksms.repository.CustomerRepository;
import com.ibsadjobah.bulksms.bulksms.repository.GroupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class BulkSmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BulkSmsApplication.class, args);

	}

	@Bean
	ModelMapper modelMapper() {
		return  new ModelMapper();
	}


	@Bean
	public CommandLineRunner groupSeeder(GroupRepository groupRepository)
	{
		return args -> {


			Group group1 = Group.builder()
					.name("Frontend")
					.build();

			Group group2 = new Group();
			group2.setName("Backend");

			Group group3 = new Group();
			group3.setName("DevOps");

			groupRepository.saveAll(Arrays.asList(group1, group2, group3));

		};
	}

	@Bean
	public CommandLineRunner customerSeeder(CustomerRepository customerRepository)
	{
		return args -> {


			Customer customer = Customer.builder()
					.id(1L)
					.name("sadio")
					.phone("621000000")
					.email("sadio@gmail.com")
					.build();

		    Customer customer1 = new Customer();

			customer1.setName("hihihi");
			customer1.setPhone("621223344");
			customer1.setEmail("hihi@gmail.com");

			customerRepository.saveAll(Arrays.asList(customer, customer1));

		};
	}

}
