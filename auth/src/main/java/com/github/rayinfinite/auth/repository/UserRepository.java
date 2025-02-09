package com.github.rayinfinite.auth.repository;

import com.github.rayinfinite.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
