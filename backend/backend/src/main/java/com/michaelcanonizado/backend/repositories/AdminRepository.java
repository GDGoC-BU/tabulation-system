package com.michaelcanonizado.backend.repositories;

import com.michaelcanonizado.backend.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
}
