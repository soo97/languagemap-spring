package kr.co.mapspring.user.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kr.co.mapspring.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignUpDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "회원가입 요청 DTO")
    public static class RequestSignUp {
    	
    	@NotBlank
        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;
    	
    	@NotBlank
        @Schema(description = "생년월일", example = "2000-01-01")
    	private LocalDate birthDate;
    	
    	@NotBlank
        @Schema(description = "주소", example = "서울시 강남구")
        private String address;
    	
    	@NotBlank
        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;
    	
    	@Email
    	@NotBlank
        @Schema(description = "이메일", example = "test@naver.com")
        private String email;
    	
    	@NotBlank
        @Schema(description = "비밀번호", example = "1234")
        private String password;
    	
    	@NotBlank
        @Schema(description = "비밀번호 확인", example = "1234")
        private String passwordConfirm;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "회원가입 응답 DTO")
    public static class ResponseSignUp {

        @Schema(description = "사용자 ID", example = "1")
        private Long userId;

        @Schema(description = "이메일", example = "test@naver.com")
        private String email;

        @Schema(description = "이름", example = "홍길동")
        private String name;

        public static ResponseSignUp from(User user) {
            return ResponseSignUp.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        }
    }
}