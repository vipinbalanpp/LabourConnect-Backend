package com.example.user.repository;

import com.example.user.model.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker,Long> {
    Worker findByEmail(String email);
}
