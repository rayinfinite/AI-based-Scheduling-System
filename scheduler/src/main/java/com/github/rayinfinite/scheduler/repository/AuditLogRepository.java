package com.github.rayinfinite.scheduler.repository;

import com.github.rayinfinite.scheduler.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findAllByOrderByIdAsc();
}
