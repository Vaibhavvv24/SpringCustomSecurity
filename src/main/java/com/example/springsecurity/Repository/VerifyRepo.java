package com.example.springsecurity.Repository;

import com.example.springsecurity.entity.VerfiyToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyRepo extends JpaRepository<VerfiyToken,Long> {
    VerfiyToken findByToken(String token);
}
