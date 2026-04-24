package kr.co.mapspring.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateScenarioDto;
import kr.co.mapspring.place.dto.AdminReadScenarioDto;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.enums.ScenarioLevel;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.impl.AdminScenarioServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminScenarioServiceTest {

    @InjectMocks
    private AdminScenarioServiceImpl scenarioService;

    @Mock
    private ScenarioRepository scenarioRepository;

    @Test
    @DisplayName("시나리오 생성 성공")
    void 시나리오_생성_성공() {
        // given
        AdminCreateScenarioDto.RequestCreate request = AdminCreateScenarioDto.RequestCreate.builder()
                .prompt("당신은 카페 직원입니다. 학습자와 영어 대화를 시작하세요.")
                .scenarioDescription("카페에서 음료를 주문하는 상황")
                .completeExp(50)
                .level(ScenarioLevel.BEGINNER)
                .category("CAFE")
                .build();

        // when
        scenarioService.createScenario(request);

        // then
        verify(scenarioRepository, times(1)).save(any(Scenario.class));
    }

    @Test
    @DisplayName("시나리오 생성 실패 생성 중 예외 발생")
    void 시나리오_저장_실패_생성중_예외발생() {
        // given
        AdminCreateScenarioDto.RequestCreate request = AdminCreateScenarioDto.RequestCreate.builder()
                .prompt("당신은 카페 직원입니다. 학습자와 영어 대화를 시작하세요.")
                .scenarioDescription("카페에서 음료를 주문하는 상황")
                .completeExp(50)
                .level(ScenarioLevel.BEGINNER)
                .category("CAFE")
                .build();

        when(scenarioRepository.save(any(Scenario.class)))
        	.thenThrow(new RuntimeException("시나리오 저장 실패"));
        
        // when & then
        assertThrows(RuntimeException.class, () -> scenarioService.createScenario(request));
        verify(scenarioRepository, times(1)).save(any(Scenario.class));
    }
    
    @Test
    @DisplayName("시나리오 조회 성공")
    void 시나리오_조회_성공() {
        // given
        Long scenarioId = 1L;

        AdminReadScenarioDto.RequestRead request = AdminReadScenarioDto.RequestRead.builder()
                .scenarioId(scenarioId)
                .build();

        Scenario scenario = Scenario.testOf(
                scenarioId,
                "당신은 카페 직원입니다. 학습자와 영어 대화를 시작하세요.",
                "카페에서 음료를 주문하는 상황",
                50,
                ScenarioLevel.BEGINNER,
                "CAFE"
        );

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.of(scenario));

        // when
        AdminReadScenarioDto.ResponseRead response = scenarioService.readScenario(request);

        // then
        assertEquals("당신은 카페 직원입니다. 학습자와 영어 대화를 시작하세요.", response.getPrompt());
        assertEquals("카페에서 음료를 주문하는 상황", response.getScenarioDescription());
        assertEquals(50, response.getCompleteExp());
        assertEquals(ScenarioLevel.BEGINNER, response.getLevel());
        assertEquals("CAFE", response.getCategory());
    }

    @Test
    @DisplayName("시나리오 조회 실패 존재하지 않는 시나리오")
    void 시나리오_조회_실패_존재하지_않는_시나리오() {
        // given
        Long scenarioId = 999L;
        
        AdminReadScenarioDto.RequestRead request = AdminReadScenarioDto.RequestRead.builder()
                .scenarioId(scenarioId)
                .build();

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ScenarioNotFoundException.class, () -> scenarioService.readScenario(request));
    }
}