package kr.co.mapspring.ai.service;

import java.util.List;

import kr.co.mapspring.ai.dto.FastApiYoutubeDto;
import kr.co.mapspring.ai.dto.ContentDto;

public interface ContentService {

	 void saveAll(
	            Long coachingSessionId,
	            String keyword,
	            List<FastApiYoutubeDto.YoutubeVideoItem> videos
	    );

    ContentDto.ResponseGetContents getContents(Long coachingSessionId);
}