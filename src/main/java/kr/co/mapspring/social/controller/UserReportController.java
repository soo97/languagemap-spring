package kr.co.mapspring.social.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.social.controller.docs.UserReportControllerDocs;
import kr.co.mapspring.social.dto.UserReportDto;
import kr.co.mapspring.social.service.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class UserReportController implements UserReportControllerDocs {

    private final UserReportService userReportService;

    @Override
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Void>> createReport(@RequestBody UserReportDto.RequestCreateReport request) {

        userReportService.createReport(
                request.getReporterId(),
                request.getReportedUserId(),
                request.getReason()
        );

        return ResponseEntity.ok(ApiResponseDTO.success("신고가 접수되었습니다.", null));
    }
}
