package kr.co.mapspring.user.dto;

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
    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
