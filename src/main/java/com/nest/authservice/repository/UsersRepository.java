package com.nest.authservice.repository;

import com.nest.authservice.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    @Override
    Optional<Users> findById(Long aLong);

    Optional<Users> findByEmail(String email);

    @Query("SELECT u FROM Users u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<Users> findByEmailWithRoles(@Param("email") String email);

}
