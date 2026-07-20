package com.example.repository;

import com.example.model.UserResume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResumeRepository extends JpaRepository<UserResume, Long> {
    // Custom queries can be added here
}