package com.whiteasher.simpleboard.auth.repository;

import com.whiteasher.simpleboard.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // SELECT
    Optional<User> findByNo(Long no);
    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);

}
