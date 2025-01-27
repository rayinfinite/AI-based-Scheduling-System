package com.github.rayinfinite.algorithm.repository;

import com.github.rayinfinite.algorithm.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
    List<Classroom> findAllByOrderByIdAsc();
}
