package cgg.jwt.springsecurity.jwt.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cgg.jwt.springsecurity.jwt.models.CustomUserDetails;
import cgg.jwt.springsecurity.jwt.repositories.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      var user=  userRepository.findByEmail(username);
      return user.map(CustomUserDetails::new).orElseThrow(()->new UsernameNotFoundException("No USer exists !!"));
    }
    
}
