package com.ibsadjobah.bulksms.bulksms;

import com.ibsadjobah.bulksms.bulksms.model.entities.Group;
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

}
