package kr.co.mapspring.ranking.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.ranking.controller.docs.RankingControllerDocs;
import kr.co.mapspring.ranking.dto.RankingDto;
import kr.co.mapspring.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController implements RankingControllerDocs {

    private final RankingService rankingService;

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<RankingDto.ResponseRanking>>> getRankings() {
        List<RankingDto.ResponseRanking> result = rankingService.getRankings();
        return ResponseEntity.ok(ApiResponseDTO.success("전체 랭킹 조회 성공", result));
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<RankingDto.ResponseRanking>> getMyRanking(@RequestParam("userId") Long userId) {
        RankingDto.ResponseRanking result = rankingService.getMyRanking(userId);
        return ResponseEntity.ok(ApiResponseDTO.success("내 랭킹 조회 성공", result));
    }
}
