package com.github.rayinfinite.scheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Classroom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer size;
    private String software;

    public Classroom(Integer id, String name, Integer size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }
}