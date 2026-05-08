package kr.co.mapspring.payment.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.payment.dto.PaymentDto;
import kr.co.mapspring.payment.enums.PaymentMethod;
import kr.co.mapspring.payment.enums.PlanType;
import kr.co.mapspring.payment.enums.SubscriptionStatus;
import kr.co.mapspring.payment.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private PaymentService paymentService;

    // << 추가: JwtTokenProvider Mock (컨트롤러에서 사용하므로 필요)
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    // 테스트용 JWT 토큰 헤더값
    private static final String BEARER_TOKEN = "Bearer test.jwt.token";

    @Test
    @WithMockUser
    @DisplayName("결제 검증 요청 - 성공")
    void verifyPayment_success() throws Exception {
        // given
        PaymentDto.RequestVerify request = new PaymentDto.RequestVerify(
                "imp_test_123",
                "order_20240101_123",
                PlanType.MONTHLY,
                PaymentMethod.KAKAOPAY,
                9900
        );

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

        // when & then
        mockMvc.perform(post("/api/payments/verify")
                        .with(csrf())
                        .header("Authorization", BEARER_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.planType").value("MONTHLY"))
                .andExpect(jsonPath("$.data.paymentAmount").value(9900));
    }

    @Test
    @WithMockUser
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
                        .with(csrf())
                        .header("Authorization", BEARER_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.planType").value("MONTHLY"))
                .andExpect(jsonPath("$.data.planStatus").value("ACTIVE"));
    }

    @Test
    @WithMockUser
    @DisplayName("구독 취소 - 성공")
    void cancelSubscription_success() throws Exception {
        // given
        given(jwtTokenProvider.getUserId(any())).willReturn(1L);
        doNothing().when(paymentService).cancelSubscription(anyLong());

        // when & then
        mockMvc.perform(delete("/api/payments/subscription")
                        .with(csrf())
                        .header("Authorization", BEARER_TOKEN))
                .andExpect(status().isOk());
    }
}