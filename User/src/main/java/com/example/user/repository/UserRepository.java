package com.example.user.repository;

import com.example.user.model.entity.Roles;
import com.example.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    List<User> findByRole(Roles role);
}
