package kr.co.mapspring.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.global.exception.CustomException;
import kr.co.mapspring.global.exception.ErrorCode;
import kr.co.mapspring.global.jwt.JwtTokenProvider;
import kr.co.mapspring.payment.controller.docs.PaymentControllerDocs;
import kr.co.mapspring.payment.dto.PaymentDto;
import kr.co.mapspring.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;

@Tag(name = "Payment", description = "결제 및 구독 관련 API")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerDocs {

    private final PaymentService paymentService;
    private final JwtTokenProvider jwtTokenProvider;

    // 토큰에서 userId 추출하는 공통 메서드
    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        String token = authHeader.substring(7);
        return jwtTokenProvider.getUserId(token);
    }

    // 결제 검증 및 구독 생성
    // 포트원 결제 완료 후 프론트에서 imp_uid를 전달하면 백엔드에서 검증 후 구독 저장
    @Override
    @PostMapping("/verify")
    public ResponseEntity<ApiResponseDTO<PaymentDto.ResponseVerify>> verifyPayment(
            HttpServletRequest request,
            @Valid @RequestBody PaymentDto.RequestVerify body
    ) {
        Long userId = extractUserId(request);
        PaymentDto.ResponseVerify response = paymentService.verifyAndSave(userId, body);
        return ResponseEntity.ok(ApiResponseDTO.success("결제 성공", response));
    }

    // 현재 활성 구독 조회
    @Override
    @GetMapping("/subscription")
    public ResponseEntity<ApiResponseDTO<PaymentDto.ResponseSubscription>> getSubscription(
            HttpServletRequest request
    ) {
        Long userId = extractUserId(request);
        PaymentDto.ResponseSubscription response = paymentService.getActiveSubscription(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("구독 조회 성공", response));
    }

    // 구독 취소
    @Override
    @DeleteMapping("/subscription")
    public ResponseEntity<ApiResponseDTO<Void>> cancelSubscription(
            HttpServletRequest request
    ) {
        Long userId = extractUserId(request);
        paymentService.cancelSubscription(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("구독 취소 성공"));
    }
}