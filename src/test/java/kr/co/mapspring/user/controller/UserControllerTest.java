package kr.co.mapspring.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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
import kr.co.mapspring.global.jwt.JwtAuthenticationFilter;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.user.dto.UserDto;
import kr.co.mapspring.user.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("유효한 토큰이면 내 정보 조회에 성공한다")
    void getMeSuccess() throws Exception {
        // given
        UserDto.ResponseMe response = UserDto.ResponseMe.builder()
                .userId(1L)
                .email("test@naver.com")
                .name("홍길동")
                .role("USER")
                .status("ACTIVE")
                .build();

        given(jwtTokenProvider.getUserId(ArgumentMatchers.any()))
                .willReturn(1L);
        given(userService.getMe(1L))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer mock-access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("내 정보 조회 성공"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@naver.com"))
                .andExpect(jsonPath("$.data.name").value("홍길동"))
                .andExpect(jsonPath("$.data.role").value("USER"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("존재하지 않는 userId면 404 실패 응답을 반환한다")
    void getMeFailWhenUserNotFound() throws Exception {
        // given
        given(jwtTokenProvider.getUserId(ArgumentMatchers.any()))
                .willReturn(999L);
        given(userService.getMe(999L))
                .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer mock-access-token"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 이메일입니다."));
    }
    
    
    @Test
    @DisplayName("유효한 토큰이면 프로필 입력에 성공한다")
    void setupProfileSuccess() throws Exception {
        // given
        UserDto.ResponseProfileSetup response = UserDto.ResponseProfileSetup.builder()
                .userId(1L)
                .email("test@gmail.com")
                .name("홍길동")
                .build();

        given(jwtTokenProvider.getUserId(ArgumentMatchers.any()))
                .willReturn(1L);
        given(userService.setupProfile(
                ArgumentMatchers.eq(1L),
                ArgumentMatchers.any(UserDto.RequestProfileSetup.class)))
                .willReturn(response);

        String requestBody = """
                {
                  "birthDate": "2000-01-01",
                  "address": "서울시 강남구",
                  "phoneNumber": "010-9999-8888"
                }
                """;

        // when & then
        mockMvc.perform(patch("/api/users/profile")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("프로필 입력 성공"))
                .andExpect(jsonPath("$.data.userId").value(1))
                .andExpect(jsonPath("$.data.email").value("test@gmail.com"))
                .andExpect(jsonPath("$.data.name").value("홍길동"));
    }

    @Test
    @DisplayName("존재하지 않는 userId면 프로필 입력 시 404 실패 응답을 반환한다")
    void setupProfileFailWhenUserNotFound() throws Exception {
        // given
        given(jwtTokenProvider.getUserId(ArgumentMatchers.any()))
                .willReturn(999L);
        given(userService.setupProfile(
                ArgumentMatchers.eq(999L),
                ArgumentMatchers.any(UserDto.RequestProfileSetup.class)))
                .willThrow(new CustomException(ErrorCode.USER_NOT_FOUND));

        String requestBody = """
                {
                  "birthDate": "2000-01-01",
                  "address": "서울시 강남구",
                  "phoneNumber": "010-9999-8888"
                }
                """;

        // when & then
        mockMvc.perform(patch("/api/users/profile")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("존재하지 않는 이메일입니다."));
    }

    @Test
    @DisplayName("생년월일 형식이 올바르지 않으면 400 실패 응답을 반환한다")
    void setupProfileFailWhenBirthDateInvalid() throws Exception {
        // given
        String requestBody = """
                {
                  "birthDate": "2000/01/01",
                  "address": "서울시 강남구",
                  "phoneNumber": "010-9999-8888"
                }
                """;

        // when & then
        mockMvc.perform(patch("/api/users/profile")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400));
    }
    
    
}