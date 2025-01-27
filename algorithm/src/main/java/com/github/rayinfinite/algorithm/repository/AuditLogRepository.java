package com.github.rayinfinite.algorithm.repository;

import com.github.rayinfinite.algorithm.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByIdAsc();
}
