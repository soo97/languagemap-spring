package kr.co.mapspring.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.co.mapspring.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LoginDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "로그인 요청 DTO")
    public static class RequestLogin {
    	
    	@NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @Schema(description = "사용자 이메일", example = "test@naver.com")
        private String email;
    	
    	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Schema(description = "사용자 비밀번호", example = "1234")
        private String password;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "로그인 응답 DTO")
    public static class ResponseLogin {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;
        
        
        @Schema(description = "사용자 이메일", example = "test@naver.com")
        private String email;

        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;

        @Schema(description = "사용자 권한", example = "USER")
        private String role;

        // Entity -> Response DTO 변환
        public static ResponseLogin from(User user) {
            return ResponseLogin.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .build();
        }
    }
}