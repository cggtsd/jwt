 package cgg.jwt.springsecurity.jwt.services;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cgg.jwt.springsecurity.jwt.models.User;
import cgg.jwt.springsecurity.jwt.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service

 public class UserService {

   @Autowired
   @Lazy
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;



        public List<User> getUsers(){
            return this.userRepository.findAll();
        }


        public User createUser(User user){

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return this.userRepository.save(user);
        }


    
}