package kr.co.mapspring.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.global.exception.GlobalExceptionHandler;
import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.dto.SignUpDto;
import kr.co.mapspring.user.service.LoginService;
import kr.co.mapspring.user.service.SignUpService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginService loginService;

    @MockitoBean
    private SignUpService signUpService;

    @Test
    @DisplayName("로그인 요청이 성공하면 공통 성공 응답을 반환한다")
    void loginSuccess() throws Exception {
        // given
        LoginDto.ResponseLogin response = LoginDto.ResponseLogin.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .role("USER")
                .build();

        given(loginService.login(ArgumentMatchers.any(LoginDto.RequestLogin.class)))
                .willReturn(response);

        String requestBody = """
                {
                  "email": "test@naver.com",
                  "password": "1234"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    @DisplayName("존재하지 않는 이메일이면 404 실패 응답을 반환한다")
    void loginFailWhenUserNotFound() throws Exception {
        // given
        given(loginService.login(ArgumentMatchers.any(LoginDto.RequestLogin.class)))
                .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        String requestBody = """
                {
                  "email": "notfound@naver.com",
                  "password": "1234"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 이메일입니다."));
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 401 실패 응답을 반환한다")
    void loginFailWhenPasswordInvalid() throws Exception {
        // given
        given(loginService.login(ArgumentMatchers.any(LoginDto.RequestLogin.class)))
                .willThrow(new CustomException(ErrorCode.INVALID_PASSWORD));

        String requestBody = """
                {
                  "email": "test@naver.com",
                  "password": "wrongPassword"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("사용자 상태가 ACTIVE가 아니면 403 실패 응답을 반환한다")
    void loginFailWhenInactiveUser() throws Exception {
        // given
        given(loginService.login(ArgumentMatchers.any(LoginDto.RequestLogin.class)))
                .willThrow(new CustomException(ErrorCode.INACTIVE_USER));

        String requestBody = """
                {
                  "email": "test@naver.com",
                  "password": "1234"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("비활성 사용자입니다."));
    }

    @Test
    @DisplayName("생년월일 형식이 올바르지 않으면 400 실패 응답을 반환한다")
    void signUpFailWhenBirthDateFormatIsInvalid() throws Exception {
        // given
    	String requestBody = """
    	        {
    	          "name": "홍길동",
    	          "birthDate": "2000/01/01",
    	          "address": "서울시 강남구",
    	          "phoneNumber": "010-1234-5678",
    	          "email": "test@naver.com",
    	          "password": "1234",
    	          "passwordConfirm": "1234",
    	          "serviceAgree": true,
    	          "privacyAgree": true,
    	          "marketingAgree": false
    	        }
    	        """;

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("입력값 검증 실패"));
    }
    @Test
    @DisplayName("회원가입 요청이 성공하면 공통 성공 응답을 반환한다")
    void signUpSuccess() throws Exception {
        // given
        SignUpDto.ResponseSignUp response = SignUpDto.ResponseSignUp.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .build();

        // 회원가입 서비스가 성공 응답을 반환한다고 가정한다.
        given(signUpService.signUp(org.mockito.ArgumentMatchers.any(SignUpDto.RequestSignUp.class)))
                .willReturn(response);

        String requestBody = """
                {
                  "name": "홍길동",
                  "birthDate": "2000-01-01",
                  "address": "서울시 강남구",
                  "phoneNumber": "010-1234-5678",
                  "email": "test@naver.com",
                  "password": "1234",
                  "passwordConfirm": "1234",
                  "serviceAgree": true,
                  "privacyAgree": true,
                  "marketingAgree": false
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("회원가입 성공"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                .andExpect(jsonPath("$.data.name").value("홍길동"));
    }

    @Test
    @DisplayName("이미 존재하는 이메일이면 409 실패 응답을 반환한다")
    void signUpFailWhenEmailAlreadyExists() throws Exception {
        // given
        given(signUpService.signUp(org.mockito.ArgumentMatchers.any(SignUpDto.RequestSignUp.class)))
                .willThrow(new kr.co.mapspring.global.exception.user.EmailAlreadyExistsException());

        String requestBody = """
                {
                  "name": "홍길동",
                  "birthDate": "2000-01-01",
                  "address": "서울시 강남구",
                  "phoneNumber": "010-1234-5678",
                  "email": "test@naver.com",
                  "password": "1234",
                  "passwordConfirm": "1234",
                  "serviceAgree": true,
                  "privacyAgree": true,
                  "marketingAgree": false
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }

    @Test
    @DisplayName("비밀번호 확인이 일치하지 않으면 400 실패 응답을 반환한다")
    void signUpFailWhenPasswordConfirmDoesNotMatch() throws Exception {
        // given
        given(signUpService.signUp(org.mockito.ArgumentMatchers.any(SignUpDto.RequestSignUp.class)))
                .willThrow(new kr.co.mapspring.global.exception.user.PasswordConfirmMismatchException());

        String requestBody = """
                {
                  "name": "홍길동",
                  "birthDate": "2000-01-01",
                  "address": "서울시 강남구",
                  "phoneNumber": "010-1234-5678",
                  "email": "test@naver.com",
                  "password": "1234",
                  "passwordConfirm": "4321",
                  "serviceAgree": true,
                  "privacyAgree": true,
                  "marketingAgree": false
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("비밀번호와 비밀번호 확인이 일치하지 않습니다."));
    }
    
    @Test
    @DisplayName("서비스 이용약관에 동의하지 않으면 400 실패 응답을 반환한다")
    void signUpFailWhenServiceTermsNotAgreed() throws Exception {
        // given
        given(signUpService.signUp(ArgumentMatchers.any(SignUpDto.RequestSignUp.class)))
                .willThrow(new CustomException(ErrorCode.SERVICE_TERMS_REQUIRED));

        String requestBody = """
                {
                  "name": "홍길동",
                  "birthDate": "2000-01-01",
                  "address": "서울시 강남구",
                  "phoneNumber": "010-1234-5678",
                  "email": "test@naver.com",
                  "password": "1234",
                  "passwordConfirm": "1234",
                  "serviceAgree": false,
                  "privacyAgree": true,
                  "marketingAgree": false
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("서비스 이용약관 동의는 필수입니다."));
    }
    
    @Test
    @DisplayName("개인정보 수집 및 이용에 동의하지 않으면 400 실패 응답을 반환한다")
    void signUpFailWhenPrivacyTermsNotAgreed() throws Exception {
        // given
        given(signUpService.signUp(ArgumentMatchers.any(SignUpDto.RequestSignUp.class)))
                .willThrow(new CustomException(ErrorCode.PRIVACY_TERMS_REQUIRED));

        String requestBody = """
                {
                  "name": "홍길동",
                  "birthDate": "2000-01-01",
                  "address": "서울시 강남구",
                  "phoneNumber": "010-1234-5678",
                  "email": "test@naver.com",
                  "password": "1234",
                  "passwordConfirm": "1234",
                  "serviceAgree": true,
                  "privacyAgree": false,
                  "marketingAgree": false
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("개인정보 수집 및 이용 동의는 필수입니다."));
    }
    
}