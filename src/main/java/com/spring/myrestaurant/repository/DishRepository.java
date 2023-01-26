package com.spring.myrestaurant.repository;

import com.spring.myrestaurant.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {

    Page<Dish> findAllByVisible(Pageable pageable, Boolean isVisible);

}
