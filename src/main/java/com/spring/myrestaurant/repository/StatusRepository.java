package com.spring.myrestaurant.repository;

import com.spring.myrestaurant.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Status findByName(String name);

}
