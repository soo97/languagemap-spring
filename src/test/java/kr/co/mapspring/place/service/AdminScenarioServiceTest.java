package kr.co.mapspring.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import kr.co.mapspring.place.dto.AdminScenarioListDto;
import kr.co.mapspring.place.dto.AdminUpdateScenarioDto;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.impl.AdminScenarioServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminScenarioServiceTest {

    @InjectMocks
    private AdminScenarioServiceImpl adminScenarioService;

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
                .category("CAFE")
                .build();

        // when
        adminScenarioService.createScenario(request);

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
                .category("CAFE")
                .build();

        when(scenarioRepository.save(any(Scenario.class)))
        	.thenThrow(new RuntimeException("시나리오 저장 실패"));
        
        // when & then
        assertThrows(RuntimeException.class, () -> adminScenarioService.createScenario(request));
        verify(scenarioRepository, times(1)).save(any(Scenario.class));
    }
    
    @Test
    @DisplayName("시나리오 상세 조회 성공")
    void 시나리오_상세_조회_성공() {
        // given
        Long scenarioId = 1L;

        Scenario scenario = Scenario.testOf(
                scenarioId,
                "당신은 카페 직원입니다. 학습자와 영어 대화를 시작하세요.",
                "카페에서 음료를 주문하는 상황",
                50,
                "CAFE"
        );

        when(scenarioRepository.findById(scenarioId))
                .thenReturn(Optional.of(scenario));

        // when
        AdminReadScenarioDto.ResponseRead response =
                adminScenarioService.readScenario(scenarioId);

        // then
        assertEquals("당신은 카페 직원입니다. 학습자와 영어 대화를 시작하세요.", response.getPrompt());
        assertEquals("카페에서 음료를 주문하는 상황", response.getScenarioDescription());
        assertEquals(50, response.getCompleteExp());
        assertEquals("CAFE", response.getCategory());

        verify(scenarioRepository, times(1)).findById(scenarioId);
    }

    @Test
    @DisplayName("시나리오 상세 조회 실패 존재하지 않는 시나리오")
    void 시나리오_상세_조회_실패_존재하지_않는_시나리오() {
        // given
        Long scenarioId = 999L;

        when(scenarioRepository.findById(scenarioId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ScenarioNotFoundException.class,
                () -> adminScenarioService.readScenario(scenarioId));

        verify(scenarioRepository, times(1)).findById(scenarioId);
    }
    
    @Test
    @DisplayName("시나리오 수정 성공")
    void 시나리오_수정_성공() {
        // given
        Long scenarioId = 1L;

        AdminUpdateScenarioDto.RequestUpdate request = AdminUpdateScenarioDto.RequestUpdate.builder()
                .prompt("수정된 프롬프트입니다.")
                .scenarioDescription("수정된 시나리오 설명입니다.")
                .completeExp(100)
                .category("RESTAURANT")
                .build();

        Scenario scenario = Scenario.testOf(
                scenarioId,
                "기존 프롬프트",
                "기존 설명",
                50,
                "CAFE"
        );

        when(scenarioRepository.findById(scenarioId))
                .thenReturn(Optional.of(scenario));

        // when
        adminScenarioService.updateScenario(scenarioId, request);

        // then
        verify(scenarioRepository, times(1)).findById(scenarioId);
    }

    @Test
    @DisplayName("시나리오 수정 실패 존재하지 않는 시나리오")
    void 시나리오_수정_실패_존재하지_않는_시나리오() {
        // given
        Long scenarioId = 999L;

        AdminUpdateScenarioDto.RequestUpdate request = AdminUpdateScenarioDto.RequestUpdate.builder()
                .prompt("수정된 프롬프트입니다.")
                .scenarioDescription("수정된 시나리오 설명입니다.")
                .completeExp(100)
                .category("RESTAURANT")
                .build();

        when(scenarioRepository.findById(scenarioId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ScenarioNotFoundException.class,
                () -> adminScenarioService.updateScenario(scenarioId, request));
    }

    @Test
    @DisplayName("시나리오 삭제 성공")
    void 시나리오_삭제_성공() {
        // given
        Long scenarioId = 1L;

        Scenario scenario = Scenario.testOf(
                scenarioId,
                "프롬프트",
                "시나리오 설명",
                50,
                "CAFE"
        );

        when(scenarioRepository.findById(scenarioId))
                .thenReturn(Optional.of(scenario));

        // when
        adminScenarioService.deleteScenario(scenarioId);

        // then
        verify(scenarioRepository, times(1)).delete(scenario);
    }

    @Test
    @DisplayName("시나리오 삭제 실패 존재하지 않는 시나리오")
    void 시나리오_삭제_실패_존재하지_않는_시나리오() {
        // given
        Long scenarioId = 999L;

        when(scenarioRepository.findById(scenarioId))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ScenarioNotFoundException.class,
                () -> adminScenarioService.deleteScenario(scenarioId));

        verify(scenarioRepository, never()).delete(any(Scenario.class));
    }
    
    @Test
    @DisplayName("시나리오 리스트 조회 성공 키워드 없음")
    void 시나리오_리스트_조회_성공_키워드_없음() {
        // given
        Scenario scenario1 = Scenario.testOf(
                1L,
                "카페 프롬프트",
                "카페에서 주문하는 상황",
                50,
                "CAFE"
        );

        Scenario scenario2 = Scenario.testOf(
                2L,
                "식당 프롬프트",
                "식당에서 주문하는 상황",
                70,
                "RESTAURANT"
        );

        when(scenarioRepository.findAll())
                .thenReturn(List.of(scenario1, scenario2));

        // when
        List<AdminScenarioListDto.ResponseList> responseList =
                adminScenarioService.scenarioList(null);

        // then
        assertEquals(2, responseList.size());
        assertEquals("CAFE", responseList.get(0).getCategory());
        assertEquals("RESTAURANT", responseList.get(1).getCategory());

        verify(scenarioRepository, times(1)).findAll();
        verify(scenarioRepository, never()).findByCategoryContaining(any());
    }
    
    @Test
    @DisplayName("시나리오 리스트 조회 성공 빈 문자열")
    void 시나리오_리스트_조회_성공_빈_문자열() {
        // given
        Scenario scenario = Scenario.testOf(
                1L,
                "카페 프롬프트",
                "카페에서 주문하는 상황",
                50,
                "CAFE"
        );

        when(scenarioRepository.findAll())
                .thenReturn(List.of(scenario));

        // when
        List<AdminScenarioListDto.ResponseList> responseList =
                adminScenarioService.scenarioList(" ");

        // then
        assertEquals(1, responseList.size());
        assertEquals("CAFE", responseList.get(0).getCategory());

        verify(scenarioRepository, times(1)).findAll();
        verify(scenarioRepository, never()).findByCategoryContaining(any());
    }
    
    @Test
    @DisplayName("시나리오 검색 성공 카테고리 키워드 있음")
    void 시나리오_검색_성공_카테고리_키워드_있음() {
        // given
        String keyword = "CAFE";

        Scenario scenario = Scenario.testOf(
                1L,
                "카페 프롬프트",
                "카페에서 주문하는 상황",
                50,
                "CAFE"
        );

        when(scenarioRepository.findByCategoryContaining(keyword))
                .thenReturn(List.of(scenario));

        // when
        List<AdminScenarioListDto.ResponseList> responseList =
                adminScenarioService.scenarioList(keyword);

        // then
        assertEquals(1, responseList.size());
        assertEquals("CAFE", responseList.get(0).getCategory());
        assertEquals("카페에서 주문하는 상황", responseList.get(0).getScenarioDescription());

        verify(scenarioRepository, times(1)).findByCategoryContaining(keyword);
        verify(scenarioRepository, never()).findAll();
    }
}