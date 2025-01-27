package com.github.rayinfinite.algorithm.repository;

import com.github.rayinfinite.algorithm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
