package kr.co.mapspring.user.dto;

import kr.co.mapspring.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
	
	private final Long userId;
    private final String email;
    private final String name;
    private final String role;
    
    public LoginResponse(Long userId, String email, String name, String role) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }
    
    public static LoginResponse from(User user) {
        return LoginResponse.builder()
                .userId(user.getUserId())         
                .email(user.getEmail())            
                .name(user.getName())             
                .role(user.getRole().name())       
                .build();                          
    }
}
