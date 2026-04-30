package kr.co.mapspring.user.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    	
    	@NotBlank(message = "이름은 필수 입력 항목입니다.")
    	@Pattern(
    	    regexp = "^[a-zA-Z가-힣]{2,}$", 
    	    message = "이름은 숫자를 제외한 최소 2자 이상 입력해주세요."
    	)
        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;
    	
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
    	
    	@Email
    	@NotBlank(message = "이메일은 입력은 필수입니다.")
        @Schema(description = "이메일", example = "test@naver.com")
        private String email;
    	
    	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    	@Size(min = 7, message = "비밀번호는 최소 7자리 이상이어야 합니다.")
    	@Pattern(
    	    regexp = "^[A-Z](?=.*[!@#$%^&*(),.?\":{}|<>]).*$",
    	    message = "첫 글자는 대문자여야 하며, 특수문자를 최소 1개 포함해야 합니다."
    	)
        private String password;
    	
    	@NotBlank
        @Schema(description = "비밀번호 확인", example = "1234")
        private String passwordConfirm;
    	
    	@Schema(description = "서비스 이용약관 동의 여부", example = "false")
        private Boolean serviceAgree;

        @Schema(description = "개인정보 수집 및 이용 동의 여부", example = "false")
        private Boolean privacyAgree;

        @Schema(description = "마케팅 정보 수신 동의 여부", example = "false")
        private Boolean marketingAgree;
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