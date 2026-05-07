package kr.co.mapspring.user.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import kr.co.mapspring.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

    @Getter
    @Builder
    @Schema(description = "내 정보 응답 DTO")
    public static class ResponseMe {

        @Schema(description = "유저 ID", example = "1")
        private Long userId;

        @Schema(description = "이메일", example = "test@naver.com")
        private String email;

        @Schema(description = "이름", example = "홍길동")
        private String name;

        @Schema(description = "역할", example = "USER")
        private String role;

        @Schema(description = "상태", example = "ACTIVE")
        private String status;

        public static ResponseMe from(User user) {
            return ResponseMe.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole().name())
                    .status(user.getStatus().name())
                    .build();
        }
    }
    
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "소셜 유저 프로필 입력 요청 DTO")
    public static class RequestProfileSetup {

    	@NotNull(message = "생년월일은 필수입니다.")
    	@Past(message = "미래 날짜는 생년월일로 사용할 수 없습니다.") 
    	@DateTimeFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "생년월일", example = "2000-01-01")
        private LocalDate birthDate;

    	@NotBlank(message = "주소를 입력해주세요.")
    	@Pattern(
    	    regexp = "^[가-힣\\s]+$", 
    	    message = "주소는 한글과 띄어쓰기만 입력 가능합니다."
    	)
        @Schema(description = "주소", example = "서울시 강남구")
        private String address;

    	@NotBlank
    	@Pattern(
    		    regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$",
    		    message = "전화번호 형식(010-0000-0000)이 올바르지 않습니다."
    		)
        @Schema(description = "전화번호", example = "010-1234-5678")
        private String phoneNumber;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "소셜 유저 프로필 입력 응답 DTO")
    public static class ResponseProfileSetup {

        @Schema(description = "유저 ID", example = "1")
        private Long userId;

        @Schema(description = "이메일", example = "test@gmail.com")
        private String email;

        @Schema(description = "이름", example = "홍길동")
        private String name;

        public static ResponseProfileSetup from(User user) {
            return ResponseProfileSetup.builder()
                    .userId(user.getUserId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        }
    }
    
    
    
    
    
    
}