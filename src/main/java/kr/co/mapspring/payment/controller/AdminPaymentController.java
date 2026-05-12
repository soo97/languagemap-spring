package kr.co.mapspring.payment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.payment.dto.AdminPaymentDto;
import kr.co.mapspring.payment.service.AdminPaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AdminPaymentDto.ResponseList>>> getPaymentList() {
        return ResponseEntity.ok(ApiResponseDTO.success("결제 목록 조회 완료",
                adminPaymentService.getPaymentList()));
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponseDTO<AdminPaymentDto.ResponseStats>> getPaymentStats() {
        return ResponseEntity.ok(ApiResponseDTO.success("결제 통계 조회 완료",
                adminPaymentService.getPaymentStats()));
    }
}