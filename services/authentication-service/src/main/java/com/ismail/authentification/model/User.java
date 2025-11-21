package com.ismail.authentification.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ismail.authentification.model.enums.Role;
import jakarta.persistence.*;

import static jakarta.persistence.EnumType.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String userName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    @Enumerated(STRING)
    private Role role;

    public User() {
        // for JPA usage
    }

    public User(String email, String userName, String password, Role role) {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
