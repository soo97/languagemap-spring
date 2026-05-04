package kr.co.mapspring.ranking.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.ranking.controller.docs.AdminRankingControllerDocs;
import kr.co.mapspring.ranking.dto.AdminRankingDto;
import kr.co.mapspring.ranking.dto.RankingDto;
import kr.co.mapspring.ranking.service.AdminRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rankings")
@RequiredArgsConstructor
public class AdminRankingController implements AdminRankingControllerDocs {

    private final AdminRankingService adminRankingService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getRankings() {
        List<RankingDto.ResponseRanking> result = adminRankingService.getRankings();

        return ResponseEntity.ok(ApiResponseDTO.success("관리자 전체 랭킹 조회 성공", result));
    }

    @Override
    @GetMapping("/weekly")
    public ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getWeeklyRankings() {
        List<RankingDto.ResponseRanking> result = adminRankingService.getWeeklyRankings();

        return ResponseEntity.ok(ApiResponseDTO.success("관리자 주간 랭킹 조회 성공", result));
    }

    @Override
    @GetMapping("/users/count")
    public ResponseEntity<ApiResponseDTO<AdminRankingDto.ResponseTotalUserCount>> getTotalUserCount() {
        Long totalUserCount = adminRankingService.getTotalUserCount();

        AdminRankingDto.ResponseTotalUserCount result =
                AdminRankingDto.ResponseTotalUserCount.from(totalUserCount);

        return ResponseEntity.ok(ApiResponseDTO.success("전체 사용자 수 조회 성공", result));
    }
}