package kr.co.mapspring.payment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import kr.co.mapspring.global.exception.GlobalExceptionHandler;
import kr.co.mapspring.global.jwt.JwtAuthenticationFilter;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.payment.dto.PaymentDto;
import kr.co.mapspring.payment.enums.PaymentMethod;
import kr.co.mapspring.payment.enums.PlanType;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import kr.co.mapspring.payment.service.PaymentService;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("결제 검증 요청 - 성공")
    void verifyPayment_success() throws Exception {
        // given
        PaymentDto.ResponseVerify response = PaymentDto.ResponseVerify.builder()
                .paymentId(1L)
                .subscriptionId(1L)
                .planType(PlanType.MONTHLY)
                .paymentAmount(9900)
                .paymentMethod(PaymentMethod.KAKAOPAY)
                .planStartAt(LocalDateTime.now())
                .planEndAt(LocalDateTime.now().plusMonths(1))
                .build();

        given(jwtTokenProvider.getUserId(any())).willReturn(1L);
        given(paymentService.verifyAndSave(anyLong(), any())).willReturn(response);

        String requestBody = """
                {
                  "impUid": "imp_test_123",
                  "merchantUid": "order_20240101_123",
                  "planType": "MONTHLY",
                  "paymentMethod": "KAKAOPAY",
                  "paymentAmount": 9900
                }
                """;

        // when & then
        mockMvc.perform(post("/api/payments/verify")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.planType").value("MONTHLY"))
                .andExpect(jsonPath("$.data.paymentAmount").value(9900));
    }

    @Test
    @DisplayName("현재 구독 조회 - 성공")
    void getSubscription_success() throws Exception {
        // given
        PaymentDto.ResponseSubscription response = PaymentDto.ResponseSubscription.builder()
                .subscriptionId(1L)
                .planType(PlanType.MONTHLY)
                .planStatus(SubscriptionStatus.ACTIVE.name())
                .planStartAt(LocalDateTime.now())
                .planEndAt(LocalDateTime.now().plusMonths(1))
                .build();

        given(jwtTokenProvider.getUserId(any())).willReturn(1L);
        given(paymentService.getActiveSubscription(anyLong())).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/payments/subscription")
                        .header("Authorization", "Bearer mock-access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.planType").value("MONTHLY"))
                .andExpect(jsonPath("$.data.planStatus").value("ACTIVE"));
    }

    @Test
    @DisplayName("구독 취소 - 성공")
    void cancelSubscription_success() throws Exception {
        // given
        given(jwtTokenProvider.getUserId(any())).willReturn(1L);
        doNothing().when(paymentService).cancelSubscription(anyLong());

        // when & then
        mockMvc.perform(delete("/api/payments/subscription")
                        .header("Authorization", "Bearer mock-access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    @DisplayName("결제 검증 - imp_uid 없으면 400 반환")
    void verifyPayment_missingImpUid_returns400() throws Exception {
        // given - impUid 누락
        String requestBody = """
                {
                  "merchantUid": "order_20240101_123",
                  "planType": "MONTHLY",
                  "paymentMethod": "KAKAOPAY",
                  "paymentAmount": 9900
                }
                """;

        // when & then
        mockMvc.perform(post("/api/payments/verify")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(400));
    }
}