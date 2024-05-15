package cgg.jwt.springsecurity.jwt.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String userId;
    private String userName;
    private String password;
    private String email;

    
}
