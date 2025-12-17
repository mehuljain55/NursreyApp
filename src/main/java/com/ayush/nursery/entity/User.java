package com.ayush.nursery.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_info")
@Data
public class User {

    @Id
    private String userId;

    @Column(unique = true)
    private String emailId;
    private String name;
    private String password;
}
