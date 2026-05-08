package kr.co.mapspring.payment.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.payment.dto.PaymentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Payment", description = "결제 및 구독 관련 API")
public interface PaymentControllerDocs {

    @Operation(summary = "결제 검증 및 구독 생성",
            description = "포트원 결제 완료 후 imp_uid를 전달받아 서버에서 검증 후 구독 정보를 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제 검증 및 구독 생성 성공"),
            @ApiResponse(responseCode = "400", description = "결제 금액 불일치 또는 결제 미완료"),
            @ApiResponse(responseCode = "409", description = "이미 처리된 결제"),
            @ApiResponse(responseCode = "500", description = "포트원 검증 실패")
    })
    ResponseEntity<ApiResponseDTO<PaymentDto.ResponseVerify>> verifyPayment(
            HttpServletRequest request,
            @Valid @RequestBody PaymentDto.RequestVerify body
    );

    @Operation(summary = "현재 구독 상태 조회",
            description = "현재 로그인한 유저의 활성 구독 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 조회 성공"),
            @ApiResponse(responseCode = "404", description = "활성 구독 없음")
    })
    ResponseEntity<ApiResponseDTO<PaymentDto.ResponseSubscription>> getSubscription(
            HttpServletRequest request
    );

    @Operation(summary = "구독 취소",
            description = "현재 활성 구독을 취소 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "구독 취소 성공"),
            @ApiResponse(responseCode = "404", description = "활성 구독 없음")
    })
    ResponseEntity<ApiResponseDTO<Void>> cancelSubscription(
            HttpServletRequest request
    );
}