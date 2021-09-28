package com.webcomm.userauthenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.webcomm.userauthenticationservice.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(String name);
}
