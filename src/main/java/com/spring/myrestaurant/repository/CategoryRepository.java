package com.spring.myrestaurant.repository;

import com.spring.myrestaurant.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
