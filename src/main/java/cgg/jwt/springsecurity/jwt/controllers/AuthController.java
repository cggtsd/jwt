package cgg.jwt.springsecurity.jwt.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cgg.jwt.springsecurity.jwt.helper.JwtHelper;
import cgg.jwt.springsecurity.jwt.helper.JwtRequest;
import cgg.jwt.springsecurity.jwt.helper.JwtResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    
    private UserDetailsService userDetailsService;
    private JwtHelper jwtHelper;
    private AuthenticationManager manager;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){

        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);
        JwtResponse jwtResponse = JwtResponse.builder().token(token).email(userDetails.getUsername()).build();
        return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authetication = new UsernamePasswordAuthenticationToken(email, password);
        try{
            manager.authenticate(authetication);
        }
        catch(BadCredentialsException e){
            throw new BadCredentialsException("Invalid username and password");
        }
    }
}
