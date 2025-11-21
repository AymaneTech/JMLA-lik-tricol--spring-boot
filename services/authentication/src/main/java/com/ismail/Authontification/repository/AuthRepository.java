package com.ismail.Authontification.repository;

import com.ismail.Authontification.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, String> {

}
