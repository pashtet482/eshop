package com.doc_byte.eshop.repository;

import com.doc_byte.eshop.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("SELECT u.username FROM User u")
    boolean existsByUsername(String username);
}
