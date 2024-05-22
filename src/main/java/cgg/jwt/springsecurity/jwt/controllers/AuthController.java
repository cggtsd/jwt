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
import cgg.jwt.springsecurity.jwt.helper.RefreshTokenRequest;
import cgg.jwt.springsecurity.jwt.models.CustomUserDetails;
import cgg.jwt.springsecurity.jwt.models.RefreshToken;
import cgg.jwt.springsecurity.jwt.models.User;
import cgg.jwt.springsecurity.jwt.services.CustomUserDetailsService;
import cgg.jwt.springsecurity.jwt.services.RefreshTokenService;
import cgg.jwt.springsecurity.jwt.services.UserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


    
    private CustomUserDetailsService customUserDetailsService;
    private JwtHelper jwtHelper;
    private AuthenticationManager manager;
    private UserService userService;
    private RefreshTokenService refreshTokenServcie;
    

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){

        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenServcie.createRefreshToken(userDetails.getUsername());
        JwtResponse jwtResponse = JwtResponse.builder().token(token).email(userDetails.getUsername()).refreshToken(refreshToken.getRefreshToken()).build();
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

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user){
        return this.userService.createUser(user);
    }

    @PostMapping("/refresh")
    public JwtResponse refreshJwtToken(@RequestBody RefreshTokenRequest request){
        RefreshToken refreshToken=  this.refreshTokenServcie.verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();
        String token = this.jwtHelper.generateToken(new CustomUserDetails(user));
        return JwtResponse.builder().refreshToken(refreshToken.getRefreshToken()).token(token).email(user.getEmail()).build();
        
    }
}
