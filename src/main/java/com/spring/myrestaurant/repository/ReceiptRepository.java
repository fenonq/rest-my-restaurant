package com.spring.myrestaurant.repository;

import com.spring.myrestaurant.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query("select r from Receipt r where r.customer.username = ?1")
    List<Receipt> findByCustomerUsername(String username);

}
