package kr.co.mapspring.ai.service;

import kr.co.mapspring.ai.dto.CoachingEntryDto;

public interface CoachingEntryService {

    CoachingEntryDto.ResponseGetCoachingEntry getCoachingEntryData(Long sessionId);
}