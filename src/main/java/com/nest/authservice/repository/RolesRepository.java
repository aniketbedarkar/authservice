package com.nest.authservice.repository;

import com.nest.authservice.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles,Long> {
}
