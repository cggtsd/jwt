package cgg.jwt.springsecurity.jwt.helper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {
    
    private String token;
    private String email;
    
}
