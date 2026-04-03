package com.yash.finance.repository;

import com.yash.finance.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserEntity,Long> {
    UserEntity findByEmail(String email);
}
