package cgg.jwt.springsecurity.jwt.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cgg.jwt.springsecurity.jwt.models.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
    
   Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
