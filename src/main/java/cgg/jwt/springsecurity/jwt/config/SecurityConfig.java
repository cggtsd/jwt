package cgg.jwt.springsecurity.jwt.config;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import cgg.jwt.springsecurity.jwt.helper.JwtAuthenticationFilter;
import cgg.jwt.springsecurity.jwt.services.UserService;
import lombok.AllArgsConstructor;

@Configuration
public class SecurityConfig {


    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter filter;

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
        .authorizeHttpRequests(auth->auth.
        requestMatchers("/auth/login").permitAll()
        .requestMatchers("/users").authenticated()
        .anyRequest().authenticated())
        //.exceptionHandling(e->e.authenticationEntryPoint(point))
        .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        

        return http.build();

    }

    @Bean
    AuthenticationManager gAuthenticationManager(AuthenticationConfiguration builder) throws Exception{

        return builder.getAuthenticationManager();
    }




    
}
