package cgg.jwt.springsecurity.jwt.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cgg.jwt.springsecurity.jwt.models.RefreshToken;
import cgg.jwt.springsecurity.jwt.models.User;
import cgg.jwt.springsecurity.jwt.repositories.RefreshTokenRepository;
import cgg.jwt.springsecurity.jwt.repositories.UserRepository;

@Service
public class RefreshTokenService {
    
    private long refreshTokenvalidity = 2*80*1000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(String username){
        User user = userRepository.findByEmail(username).get();
        RefreshToken refreshToken = user.getRefreshToken();

        if(refreshToken==null){
             refreshToken = RefreshToken.builder().refreshToken(UUID.randomUUID().toString())
            .expiry(Instant.now().plusMillis(refreshTokenvalidity)).user(user).build();
        }
        else{
            refreshToken.setExpiry(Instant.now().plusMillis(refreshTokenvalidity));
        }

        user.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){

        RefreshToken rt = refreshTokenRepository.findByRefreshToken(refreshToken)
        .orElseThrow(()->new RuntimeException("Given token does not exists in db"));

        if(rt.getExpiry().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(rt);
            throw new RuntimeException("Refresh Token Expired !!");
        }

        return rt;

    }
}
