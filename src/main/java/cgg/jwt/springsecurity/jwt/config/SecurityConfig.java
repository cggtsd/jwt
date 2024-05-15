package cgg.jwt.springsecurity.jwt.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import cgg.jwt.springsecurity.jwt.services.UserService;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {


    private UserService userService;



    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(){


        Collection<UserDetails> users= 
        userService.getUsers().stream().map(user->{
                return User.builder().username(user.getUserName()).password(passwordEncoder().encode(user.getPassword())).roles("ADMIN").build();
            }).collect(Collectors.toList());

      //  UserDetails user=User.builder().username("prem").password(passwordEncoder().encode("prem")).roles("ADMIN").build();
        return new InMemoryUserDetailsManager(users);
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
        .csrf(csrf->csrf.disable())
        .authorizeHttpRequests(auth->auth.anyRequest().authenticated())
        .formLogin(Customizer.withDefaults());

        return http.build();

    }





    
}
