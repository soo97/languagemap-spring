package kr.co.mapspring.social.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.social.controller.docs.AdminSocialControllerDocs;
import kr.co.mapspring.social.dto.AdminSocialDto;
import kr.co.mapspring.social.service.AdminSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/social")
public class AdminSocialController implements AdminSocialControllerDocs {

    private final AdminSocialService adminSocialService;

    @Override
    @GetMapping("/reports")
    public ResponseEntity<ApiResponseDTO<List<AdminSocialDto.ResponseReport>>> getReports() {
        List<AdminSocialDto.ResponseReport> result = adminSocialService.getReports();

        return ResponseEntity.ok(ApiResponseDTO.success("신고 목록 조회 성공", result));
    }

    @Override
    @GetMapping("/reports/{reportId}")
    public ResponseEntity<ApiResponseDTO<AdminSocialDto.ResponseReport>> getReport(@PathVariable("reportId") Long reportId) {
        AdminSocialDto.ResponseReport result = adminSocialService.getReport(reportId);

        return ResponseEntity.ok(ApiResponseDTO.success("신고 상세 조회 성공", result));
    }

    @Override
    @PatchMapping("/reports/{reportId}/status")
    public ResponseEntity<ApiResponseDTO<Void>> updateReportStatus(@PathVariable("reportId") Long reportId,
                                                                   @RequestBody AdminSocialDto.RequestUpdateReportStatus request) {
        adminSocialService.updateReportStatus(reportId, request);

        return ResponseEntity.ok(ApiResponseDTO.success("신고 상태 변경 성공", null));
    }

    @Override
    @GetMapping("/friendships/blocked")
    public ResponseEntity<ApiResponseDTO<List<AdminSocialDto.ResponseFriendshipHistory>>> getBlockedFriendships() {
        List<AdminSocialDto.ResponseFriendshipHistory> result = adminSocialService.getBlockedFriendships();

        return ResponseEntity.ok(ApiResponseDTO.success("차단 이력 조회 성공", result));
    }

    @Override
    @GetMapping("/friendships/rejected")
    public ResponseEntity<ApiResponseDTO<List<AdminSocialDto.ResponseFriendshipHistory>>> getRejectedFriendships() {
        List<AdminSocialDto.ResponseFriendshipHistory> result =
                adminSocialService.getRejectedFriendships();

        return ResponseEntity.ok(ApiResponseDTO.success("거절 이력 조회 성공", result));
    }
}
