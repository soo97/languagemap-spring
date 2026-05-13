package kr.co.mapspring.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.user.controller.docs.AdminUserControllerDocs;
import kr.co.mapspring.user.dto.AdminUserDto;
import kr.co.mapspring.user.service.AdminUserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController implements AdminUserControllerDocs {

    private final AdminUserService adminUserService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<AdminUserDto.ResponseList>>> getUserList(
            @RequestParam(name = "keyword", required = false) String keyword) {
        return ResponseEntity.ok(ApiResponseDTO.success("회원 목록 조회 완료",
                adminUserService.getUserList(keyword)));
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO<AdminUserDto.ResponseDetail>> getUserDetail(
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(ApiResponseDTO.success("회원 상세 조회 완료",
                adminUserService.getUserDetail(userId)));
    }

    @Override
    @PatchMapping("/{userId}/status")
    public ResponseEntity<ApiResponseDTO<Void>> updateUserStatus(
            @PathVariable("userId") Long userId,
            @RequestBody AdminUserDto.RequestUpdateStatus request) {
        adminUserService.updateUserStatus(userId, request);
        return ResponseEntity.ok(ApiResponseDTO.success("회원 상태 변경 완료"));
    }
}