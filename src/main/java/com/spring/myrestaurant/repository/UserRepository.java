package com.spring.myrestaurant.repository;

import com.spring.myrestaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(@Param("username") String username);

    @Modifying
    @Transactional
    @Query("update User u set u.active = :active where u.id = :id")
    void changUserStatus(@Param("id") Long id, @Param("active") boolean active);

}
