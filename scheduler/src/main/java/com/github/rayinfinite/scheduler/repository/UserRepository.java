package com.github.rayinfinite.scheduler.repository;

import com.github.rayinfinite.scheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
