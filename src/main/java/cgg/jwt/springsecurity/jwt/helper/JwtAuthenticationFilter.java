package cgg.jwt.springsecurity.jwt.helper;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter{


    private HandlerExceptionResolver handlerExceptionResolver;

    
    public JwtAuthenticationFilter(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Autowired
    @Lazy
    private JwtHelper jwtHelper;
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;
   
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       String header = request.getHeader("Authorization");
       log.info(" Header : {} ",header);
       String username=null;
       String token=null;

       try{
        if(header!=null && header.startsWith("Bearer")){
            token=  header.substring(7);
            //  try{
                 username= jwtHelper.getUsernameFromToken(token);
           //  }
            //  catch(IllegalArgumentException e){
            //      log.info("Illegal argument while fetching the username !! ");
            //      e.printStackTrace();
            //  }
            //  catch(ExpiredJwtException e){
            //      log.info("Given Jwt token is expired !!");
            //      e.printStackTrace();
            //  }
            //  catch(MalformedJwtException e){
            //      System.out.println("Corrupted Token..");
            //      log.info("Some changes has been done in token !! Invlaid Token !!");
            //      e.printStackTrace();
            //  }
            //  catch(Exception e){
            //      e.printStackTrace();
            //  }
            }
            else{
             log.info("Invalid Header Value !!");
            }
     
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
               UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
               Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
     
               if(validateToken){
                 //set the authentication
                 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, validateToken,userDetails.getAuthorities());
                WebAuthenticationDetails details = new WebAuthenticationDetailsSource().buildDetails(request);
                  authentication.setDetails(details);
                  SecurityContextHolder.getContext().setAuthentication(authentication);
     
               }
               else{
                 log.info(" Validation fails !!");
               }
            }
            filterChain.doFilter(request, response);
       }
       catch(Exception ex){
        handlerExceptionResolver.resolveException(request, response, null, ex);
       }
      
    }



}