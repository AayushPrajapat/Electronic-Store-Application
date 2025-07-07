package com.lcwd.electronic.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.repositories.RoleRepository;

@SpringBootApplication
@EnableWebMvc  // kisi bhi configuration class pe laga skte hai... this annotation from swagger docs
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);

	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@Value("${normal.role.id}")
	private String roleNormalId;

	@Value("${admin.role.id}")
	private String roleAdminId;

	@Override
	public void run(String... args) throws Exception {

		// System.out.println(passwordEncoder.encode("12345"));

		try {

//			Role role_admin = Role.builder().roleId(roleAdminId).roleName("ROLE_ADMIN").build();
//			Role role_normal = Role.builder().roleId(roleNormalId).roleName("ROLE_NORMAL").build();
//
//			Role savedAdmin = this.roleRepository.save(role_admin);
//			Role savedNormal = this.roleRepository.save(role_normal);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
