package kr.co.mapspring.place.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.global.exception.place.ScenarioNotFoundException;
import kr.co.mapspring.place.dto.AdminCreateMissionDto;
import kr.co.mapspring.place.entity.Mission;
import kr.co.mapspring.place.entity.Scenario;
import kr.co.mapspring.place.repository.MissionRepository;
import kr.co.mapspring.place.repository.ScenarioRepository;
import kr.co.mapspring.place.service.impl.AdminMissionServiceImpl;

@ExtendWith(MockitoExtension.class)
class AdminMissionServiceTest {

    @InjectMocks
    private AdminMissionServiceImpl adminMissionService;

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private ScenarioRepository scenarioRepository;

    @Test
    @DisplayName("미션 생성 성공")
    void 미션_생성_성공() {
        // given
        AdminCreateMissionDto.RequestCreate request = AdminCreateMissionDto.RequestCreate.builder()
                .missionTitle("카페에서 주문하기")
                .missionDescription("카페에서 영어로 커피를 주문하는 미션")
                .scenarioId(1L)
                .build();

        Scenario scenario = Scenario.withId(1L);

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.of(scenario));

        // when
        adminMissionService.createMission(request);

        // then
        verify(missionRepository, times(1)).save(any(Mission.class));
    }

    @Test
    @DisplayName("미션 생성 실패 존재하지 않는 시나리오")
    void 미션_생성_실패_존재하지_않는_시나리오() {
        // given
        AdminCreateMissionDto.RequestCreate request = AdminCreateMissionDto.RequestCreate.builder()
                .missionTitle("카페에서 주문하기")
                .missionDescription("카페에서 영어로 커피를 주문하는 미션")
                .scenarioId(999L)
                .build();

        when(scenarioRepository.findById(request.getScenarioId()))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(ScenarioNotFoundException.class,
                () -> adminMissionService.createMission(request));

        verify(missionRepository, never()).save(any(Mission.class));
    }
}
