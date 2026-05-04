package kr.co.mapspring.ai.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.mapspring.ai.dto.ContentDto;
import kr.co.mapspring.ai.dto.FastApiYoutubeDto;
import kr.co.mapspring.ai.entity.Content;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.repository.ContentRepository;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.ContentService;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final CoachingSessionRepository coachingSessionRepository;

    @Override
    @Transactional
    public void saveAll(
            Long coachingSessionId,
            String keyword,
            List<FastApiYoutubeDto.YoutubeVideoItem> videos
    ) {
        CoachingSession session = coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);

        List<Content> contents = videos.stream()
                .map(v -> Content.create(
                        session,
                        keyword,
                        v.getTitle(),
                        v.getChannelTitle(),
                        v.getThumbnailUrl(),
                        v.getVideoUrl(),
                        v.getSummary()
                ))
                .toList();

        contentRepository.saveAll(contents);
    }

    @Override
    public ContentDto.ResponseGetContents getContents(Long coachingSessionId) {

        List<ContentDto.ResponseContent> contents = contentRepository
                .findByCoachingSession_CoachingSessionId(coachingSessionId)
                .stream()
                .map(ContentDto.ResponseContent::from)
                .toList();

        return ContentDto.ResponseGetContents.builder()
                .coachingSessionId(coachingSessionId)
                .contents(contents)
                .build();
    }
}