package cgg.jwt.springsecurity.jwt.exceptions;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class CustomExceptionHandler {
   
    
    @ExceptionHandler(Exception.class)
    public ProblemDetail exceptionHandler(Exception ex){

        ProblemDetail problemDetail=null;

        if(ex instanceof BadCredentialsException){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),ex.getMessage());
            problemDetail.setProperty("access_denied_reason", "Authentication Failed");
        }
        if(ex instanceof AccessDeniedException){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),ex.getMessage());
            problemDetail.setProperty("access_denied_reason", "Not Authorized");
        }
        if(ex instanceof SignatureException){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),ex.getMessage());
            problemDetail.setProperty("access_denied_reason", "Invalid Signature");
        }
        
        if(ex instanceof MalformedJwtException){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),ex.getMessage());
            problemDetail.setProperty("access_denied_reason", "Invalid Token !!");
        }
        if(ex instanceof ExpiredJwtException){
            problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403),ex.getMessage());
            problemDetail.setProperty("access_denied_reason", "Token Expired !!");
        }

        return problemDetail;
    }
}
