package com.example.springsecurity.Repository;

import com.example.springsecurity.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepo extends JpaRepository<PasswordResetToken,Long> {
     PasswordResetToken findByToken(String pwdresetToken) ;
}
