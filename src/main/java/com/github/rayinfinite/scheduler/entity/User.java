package com.github.rayinfinite.scheduler.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "app_user")
public class User {
    @Id
    private String email;
    private String name;
    private String role;
    private String phone;
    private boolean active;
    private Date created;
    private Date lastLogin;
}
