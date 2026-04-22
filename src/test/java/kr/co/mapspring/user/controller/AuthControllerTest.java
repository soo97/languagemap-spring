package kr.co.mapspring.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.mapspring.user.dto.LoginDto;
import kr.co.mapspring.user.service.LoginService;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    // 가짜 HTTP 요청을 보내고 응답을 검증하는 도구
    @Autowired
    private MockMvc mockMvc;

    // AuthController 내부에서 사용하는 LoginService를 mock으로 대체한다.
    // 즉 실제 로그인 로직은 안 타고, 컨트롤러 동작만 본다.
    @MockitoBean
    private LoginService loginService;

    @Test
    @DisplayName("로그인 요청이 성공하면 공통 성공 응답을 반환한다")
    void loginSuccess() throws Exception {
        // given
        // 서비스가 성공적으로 로그인했을 때 반환할 응답 객체를 미리 만든다.
        LoginDto.ResponseLogin response = LoginDto.ResponseLogin.builder()
                .userId(1L)                     // 응답 userId
                .email("test@naver.com")       // 응답 email
                .name("홍길동")                  // 응답 name
                .role("USER")                  // 응답 role
                .build();

        // loginService.login(...)이 호출되면 위 response를 반환하도록 mock 동작을 지정한다.
        // any(LoginDto.RequestLogin.class)는 어떤 RequestLogin이 와도 상관없다는 뜻이다.
        given(loginService.login(org.mockito.ArgumentMatchers.any(LoginDto.RequestLogin.class)))
                .willReturn(response);

        // 실제 HTTP 요청 body로 보낼 JSON 문자열이다.
        // email, password를 담아서 컨트롤러에 전송한다.
        String requestBody = """
                {
                  "email": "test@naver.com",
                  "password": "1234"
                }
                """;

        // when & then
        // /api/auth/login 으로 POST 요청을 보낸다.
        mockMvc.perform(post("/api/auth/login")
                        // 요청의 Content-Type이 JSON임을 명시한다.
                        .contentType(APPLICATION_JSON)
                        // 위에서 만든 JSON 문자열을 요청 body로 넣는다.
                        .content(requestBody))
                // 응답 HTTP status가 200 OK인지 확인
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.role").value("USER"));
    }
}