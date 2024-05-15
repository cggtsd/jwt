 package cgg.jwt.springsecurity.jwt.services;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cgg.jwt.springsecurity.jwt.models.User;

@Service
 public class UserService {

    private List<User> users= Arrays.asList(
        new User(UUID.randomUUID().toString(),"prem","prem","prem@gmail.com"),
        new User(UUID.randomUUID().toString(),"chandh","chandh","chandh@gmail.com"),
        new User(UUID.randomUUID().toString(),"maneesha","maneesha","maneesha@gmail.com")

        );



        public List<User> getUsers(){
            return this.users;
        }




    
}