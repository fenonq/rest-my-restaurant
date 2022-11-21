package com.spring.myrestaurant.repository;

import com.spring.myrestaurant.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
