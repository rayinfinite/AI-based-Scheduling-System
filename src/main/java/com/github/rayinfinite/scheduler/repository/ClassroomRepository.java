package com.github.rayinfinite.scheduler.repository;

import com.github.rayinfinite.scheduler.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
}