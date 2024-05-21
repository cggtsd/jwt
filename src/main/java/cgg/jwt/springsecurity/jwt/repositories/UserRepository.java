package cgg.jwt.springsecurity.jwt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cgg.jwt.springsecurity.jwt.models.User;


public interface UserRepository extends JpaRepository<User,String> {
 
    Optional<User> findByEmail(String email);
    
}
