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
import kr.co.mapspring.user.service.LoginService;

@WebMvcTest(AuthController.class)
// Security filter 때문에 401/403이 뜨면 이 설정이 도움이 된다.
@AutoConfigureMockMvc(addFilters = false)
// 전역 예외 처리기도 같이 테스트에 포함시킨다.
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // 컨트롤러가 의존하는 서비스는 mock으로 대체한다.
    @MockitoBean
    private LoginService loginService;

    @Test
    @DisplayName("로그인 요청이 성공하면 공통 성공 응답을 반환한다")
    void loginSuccess() throws Exception {
        // given
        // 서비스가 정상 로그인 결과를 반환한다고 가정한다.
        LoginDto.ResponseLogin response = LoginDto.ResponseLogin.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .role("USER")
                .build();

        // 어떤 로그인 요청이 오든 위 응답을 반환하도록 mock 설정
        given(loginService.login(ArgumentMatchers.any(LoginDto.RequestLogin.class)))
                .willReturn(response);

        // 실제 요청 body 역할을 하는 JSON 문자열
        String requestBody = """
                {
                  "email": "test@naver.com",
                  "password": "1234"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        // 요청 데이터가 JSON임을 명시
                        .contentType(APPLICATION_JSON)
                        // 요청 body 삽입
                        .content(requestBody))
                // HTTP 상태코드 200 확인
                .andExpect(status().isOk())
                // 공통 응답 구조 확인
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                // 실제 data 값 확인
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }

    @Test
    @DisplayName("존재하지 않는 이메일이면 404 실패 응답을 반환한다")
    void loginFailWhenUserNotFound() throws Exception {
        // given
        // 서비스가 USER_NOT_FOUND 예외를 던진다고 가정한다.
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
                // USER_NOT_FOUND는 ErrorCode 기준 404
                .andExpect(status().isNotFound())
                // 공통 실패 응답 구조 확인
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 이메일입니다."));
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 401 실패 응답을 반환한다")
    void loginFailWhenPasswordInvalid() throws Exception {
        // given
        // 서비스가 INVALID_PASSWORD 예외를 던진다고 가정한다.
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
                // INVALID_PASSWORD는 ErrorCode 기준 401
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("사용자 상태가 ACTIVE가 아니면 403 실패 응답을 반환한다")
    void loginFailWhenInactiveUser() throws Exception {
        // given
        // 서비스가 INACTIVE_USER 예외를 던진다고 가정한다.
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
                // INACTIVE_USER는 ErrorCode 기준 403
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
                  "passwordConfirm": "1234"
                }
                """;

        // when & then
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("생년월일 형식이 올바르지 않습니다. yyyy-MM-dd 형식으로 입력해주세요."));
    }
    
}