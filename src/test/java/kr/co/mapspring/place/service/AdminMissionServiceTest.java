package kr.co.mapspring.place.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import kr.co.mapspring.global.exception.place.MissionNotFoundException;
import kr.co.mapspring.place.dto.AdminMissionListDto;
import kr.co.mapspring.place.dto.AdminReadMissionDto;
import kr.co.mapspring.place.dto.AdminUpdateMissionDto;
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

	@Test
	@DisplayName("미션 상세 조회 성공")
	void 미션_조회_성공() {
		Scenario scenario = Scenario.withId(1L);

		Mission mission = Mission.create("카페", "설명", scenario);

		when(missionRepository.findById(1L))
				.thenReturn(Optional.of(mission));

		AdminReadMissionDto.ResponseRead result =
				adminMissionService.readMission(1L);

		assertEquals("카페", result.getMissionTitle());
		assertEquals("설명", result.getMissionDescription());
	}

	@Test
	@DisplayName("미션 상세 조회 실패")
	void 미션_조회_실패() {
		when(missionRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(MissionNotFoundException.class,
				() -> adminMissionService.readMission(1L));
	}

	// ===== 리스트 =====
	@Test
	@DisplayName("미션 리스트 조회 성공")
	void 미션_리스트() {
		Scenario scenario = Scenario.withId(1L);

		Mission m1 = Mission.create("카페", "설명1", scenario);
		Mission m2 = Mission.create("식당", "설명2", scenario);

		when(missionRepository.findAll())
				.thenReturn(List.of(m1, m2));

		List<AdminMissionListDto.ResponseList> result =
				adminMissionService.missionList(null);

		assertEquals(2, result.size());

		verify(missionRepository).findAll();
		verify(missionRepository, never()).findByMissionTitleContaining(any());
	}

	@Test
	@DisplayName("미션 검색 성공")
	void 미션_검색() {
		Scenario scenario = Scenario.withId(1L);

		Mission mission = Mission.create("카페 주문", "설명", scenario);

		when(missionRepository.findByMissionTitleContaining("카페"))
				.thenReturn(List.of(mission));

		List<AdminMissionListDto.ResponseList> result =
				adminMissionService.missionList("카페");

		assertEquals(1, result.size());

		verify(missionRepository).findByMissionTitleContaining("카페");
		verify(missionRepository, never()).findAll();
	}

	// ===== 수정 =====
	@Test
	@DisplayName("미션 수정 성공")
	void 미션_수정() {
		Scenario oldScenario = Scenario.withId(1L);
		Scenario newScenario = Scenario.withId(2L);

		Mission mission = Mission.create("기존", "설명", oldScenario);

		AdminUpdateMissionDto.RequestUpdate request =
				AdminUpdateMissionDto.RequestUpdate.builder()
						.missionTitle("수정")
						.missionDescription("수정설명")
						.scenarioId(2L)
						.build();

		when(missionRepository.findById(1L))
				.thenReturn(Optional.of(mission));

		when(scenarioRepository.findById(2L))
				.thenReturn(Optional.of(newScenario));

		adminMissionService.updateMission(1L, request);

		assertEquals("수정", mission.getMissionTitle());
		assertEquals("수정설명", mission.getMissionDescription());
		assertEquals(newScenario, mission.getScenario());
	}

	@Test
	@DisplayName("미션 수정 실패 미션 없음")
	void 미션_수정_실패1() {
		when(missionRepository.findById(1L))
				.thenReturn(Optional.empty());

		AdminUpdateMissionDto.RequestUpdate request =
				AdminUpdateMissionDto.RequestUpdate.builder()
						.missionTitle("수정")
						.missionDescription("설명")
						.scenarioId(1L)
						.build();

		assertThrows(MissionNotFoundException.class,
				() -> adminMissionService.updateMission(1L, request));
	}

	@Test
	@DisplayName("미션 수정 실패 시나리오 없음")
	void 미션_수정_실패2() {
		Mission mission = Mission.create("기존", "설명", Scenario.withId(1L));

		when(missionRepository.findById(1L))
				.thenReturn(Optional.of(mission));

		when(scenarioRepository.findById(999L))
				.thenReturn(Optional.empty());

		AdminUpdateMissionDto.RequestUpdate request =
				AdminUpdateMissionDto.RequestUpdate.builder()
						.missionTitle("수정")
						.missionDescription("설명")
						.scenarioId(999L)
						.build();

		assertThrows(ScenarioNotFoundException.class,
				() -> adminMissionService.updateMission(1L, request));
	}

	// ===== 삭제 =====
	@Test
	@DisplayName("미션 삭제 성공")
	void 미션_삭제() {
		Mission mission = Mission.create("제목", "설명", Scenario.withId(1L));

		when(missionRepository.findById(1L))
				.thenReturn(Optional.of(mission));

		adminMissionService.deleteMission(1L);

		verify(missionRepository).delete(mission);
	}

	@Test
	@DisplayName("미션 삭제 실패")
	void 미션_삭제_실패() {
		when(missionRepository.findById(1L))
				.thenReturn(Optional.empty());

		assertThrows(MissionNotFoundException.class,
				() -> adminMissionService.deleteMission(1L));

		verify(missionRepository, never()).delete(any());
	}
}
