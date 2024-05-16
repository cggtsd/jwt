package cgg.jwt.springsecurity.jwt.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import cgg.jwt.springsecurity.jwt.models.User;
import cgg.jwt.springsecurity.jwt.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers(){
        return this.userService.getUsers();
    }

    @GetMapping("/current-user")
    public String getLoggedInUser(Principal principal)
{
    return principal.getName();
}
    
}
