package com.ismail.authentification.repository;

import com.ismail.authentification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndPassword(String email, String password);

    Boolean existsByEmail(String email);
}
