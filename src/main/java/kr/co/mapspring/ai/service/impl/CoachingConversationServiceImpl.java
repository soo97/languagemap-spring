package kr.co.mapspring.ai.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.mapspring.ai.client.FastApiOpenAiClient;
import kr.co.mapspring.ai.client.FastApiSpeechClient;
import kr.co.mapspring.ai.client.FastApiYoutubeClient;
import kr.co.mapspring.ai.dto.CoachingConversationDto;
import kr.co.mapspring.ai.dto.CoachingFeedbackDto;
import kr.co.mapspring.ai.dto.CoachingMessageDto;
import kr.co.mapspring.ai.dto.CoachingPronunciationResultDto;
import kr.co.mapspring.ai.dto.CoachingScriptTurnDto;
import kr.co.mapspring.ai.dto.ContentDto;
import kr.co.mapspring.ai.dto.FastApiOpenAiDto;
import kr.co.mapspring.ai.dto.FastApiSpeechDto;
import kr.co.mapspring.ai.dto.FastApiYoutubeDto;
import kr.co.mapspring.ai.dto.StartCoachingSessionDto;
import kr.co.mapspring.ai.entity.CoachingSession;
import kr.co.mapspring.ai.enums.CoachingMessageRole;
import kr.co.mapspring.ai.repository.CoachingSessionRepository;
import kr.co.mapspring.ai.service.CoachingConversationService;
import kr.co.mapspring.ai.service.CoachingFeedbackService;
import kr.co.mapspring.ai.service.CoachingMessageService;
import kr.co.mapspring.ai.service.CoachingPronunciationResultService;
import kr.co.mapspring.ai.service.CoachingScriptTurnService;
import kr.co.mapspring.ai.service.ContentService;
import kr.co.mapspring.ai.service.StartCoachingSessionService;
import kr.co.mapspring.global.exception.ai.CoachingScriptTurnNotFoundException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingConversationServiceImpl implements CoachingConversationService {

    private final FastApiOpenAiClient fastApiOpenAiClient;
    private final FastApiSpeechClient fastApiSpeechClient;
    private final FastApiYoutubeClient fastApiYoutubeClient;

    private final StartCoachingSessionService startCoachingSessionService;
    private final CoachingScriptTurnService coachingScriptTurnService;
    private final CoachingMessageService coachingMessageService;
    private final CoachingPronunciationResultService pronunciationResultService;
    private final CoachingFeedbackService coachingFeedbackService;
    private final ContentService contentService;

    private final CoachingSessionRepository coachingSessionRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public CoachingConversationDto.ResponsePrepareScript prepareScript(
            CoachingConversationDto.RequestPrepareScript request
    ) {
        StartCoachingSessionDto.ResponseStartCoachingSession sessionResponse =
                startCoachingSessionService.startCoachingSession(
                        StartCoachingSessionDto.RequestStartCoachingSession.builder()
                                .sessionId(request.getSessionId())
                                .optionType(request.getOptionType())
                                .build()
                );

        FastApiOpenAiDto.ResponseCoachingScript scriptResponse =
                fastApiOpenAiClient.createCoachingScript(
                        FastApiOpenAiDto.RequestCoachingScript.builder()
                                .optionType(request.getOptionType())
                                .placeName(request.getPlaceName())
                                .country(request.getCountry())
                                .city(request.getCity())
                                .placeAddress(request.getPlaceAddress())
                                .evaluation(request.getEvaluation())
                                .previousMessages(request.getPreviousMessages())
                                .build()
                );

        List<CoachingScriptTurnDto.RequestSaveCoachingScriptTurn> turnRequests =
                convertMessagesToScriptTurns(
                        sessionResponse.getCoachingSessionId(),
                        scriptResponse.getMessages()
                );

        List<CoachingScriptTurnDto.ResponseCoachingScriptTurn> savedTurns =
                coachingScriptTurnService.saveScriptTurns(
                        sessionResponse.getCoachingSessionId(),
                        turnRequests
                );

        return CoachingConversationDto.ResponsePrepareScript.builder()
                .coachingSessionId(sessionResponse.getCoachingSessionId())
                .sessionId(sessionResponse.getSessionId())
                .coachingSessionStatus(sessionResponse.getCoachingSessionStatus())
                .selectedOption(sessionResponse.getSelectedOption())
                .currentTurnOrder(sessionResponse.getCurrentTurnOrder())
                .turns(savedTurns)
                .build();
    }

    @Override
    @Transactional
    public CoachingConversationDto.ResponseStartConversation startConversation(
            Long coachingSessionId
    ) {
        CoachingSession session = getSession(coachingSessionId);

        CoachingScriptTurnDto.ResponseCoachingScriptTurn firstTurn =
                coachingScriptTurnService.getScriptTurn(coachingSessionId, 1);

        FastApiSpeechDto.ResponseTts ttsResponse =
                fastApiSpeechClient.createTts(
                        FastApiSpeechDto.RequestTts.builder()
                                .text(firstTurn.getAssistantText())
                                .build()
                );

        coachingMessageService.saveAssistantMessage(
                coachingSessionId,
                firstTurn.getCoachingScriptTurnId(),
                firstTurn.getAssistantText(),
                ttsResponse.getAudioUrl()
        );

        session.updateCurrentTurnOrder(1);

        return CoachingConversationDto.ResponseStartConversation.builder()
                .coachingSessionId(coachingSessionId)
                .coachingScriptTurnId(firstTurn.getCoachingScriptTurnId())
                .turnOrder(firstTurn.getTurnOrder())
                .assistantText(firstTurn.getAssistantText())
                .assistantAudioUrl(ttsResponse.getAudioUrl())
                .build();
    }

    @Override
    @Transactional
    public CoachingConversationDto.ResponseConversationTurn processUserSpeech(
            Long coachingSessionId,
            MultipartFile audioFile
    ) {
        CoachingSession session = getSession(coachingSessionId);

        Integer currentTurnOrder = session.getCurrentTurnOrder();

        CoachingScriptTurnDto.ResponseCoachingScriptTurn currentTurn =
                coachingScriptTurnService.getScriptTurn(
                        coachingSessionId,
                        currentTurnOrder
                );

        FastApiSpeechDto.ResponsePronunciationAssessment pronunciation =
                fastApiSpeechClient.assessPronunciation(
                        currentTurn.getExpectedText(),
                        audioFile
                );

        CoachingMessageDto.ResponseCoachingMessage savedUserMessage =
                coachingMessageService.saveUserMessage(
                        coachingSessionId,
                        currentTurn.getCoachingScriptTurnId(),
                        pronunciation.getRecognizedText(),
                        null
                );

        pronunciationResultService.savePronunciationResult(
                CoachingPronunciationResultDto.RequestSavePronunciationResult.builder()
                        .coachingMessageId(savedUserMessage.getCoachingMessageId())
                        .coachingScriptTurnId(currentTurn.getCoachingScriptTurnId())
                        .recognizedText(pronunciation.getRecognizedText())
                        .accuracyScore(pronunciation.getAccuracyScore())
                        .fluencyScore(pronunciation.getFluencyScore())
                        .completenessScore(pronunciation.getCompletenessScore())
                        .pronunciationScore(pronunciation.getPronunciationScore())
                        .feedback(pronunciation.getFeedback())
                        .problemWords(toJson(pronunciation.getProblemWords()))
                        .build()
        );

        int nextTurnOrder = currentTurnOrder + 1;

        try {
            CoachingScriptTurnDto.ResponseCoachingScriptTurn nextTurn =
                    coachingScriptTurnService.getScriptTurn(
                            coachingSessionId,
                            nextTurnOrder
                    );

            FastApiSpeechDto.ResponseTts nextTts =
                    fastApiSpeechClient.createTts(
                            FastApiSpeechDto.RequestTts.builder()
                                    .text(nextTurn.getAssistantText())
                                    .build()
                    );

            coachingMessageService.saveAssistantMessage(
                    coachingSessionId,
                    nextTurn.getCoachingScriptTurnId(),
                    nextTurn.getAssistantText(),
                    nextTts.getAudioUrl()
            );

            session.updateCurrentTurnOrder(nextTurnOrder);

            return CoachingConversationDto.ResponseConversationTurn.builder()
                    .coachingSessionId(coachingSessionId)
                    .userMessageId(savedUserMessage.getCoachingMessageId())
                    .recognizedText(pronunciation.getRecognizedText())
                    .userFeedback(pronunciation.getFeedback())
                    .problemWords(pronunciation.getProblemWords())
                    .accuracyScore(pronunciation.getAccuracyScore())
                    .fluencyScore(pronunciation.getFluencyScore())
                    .completenessScore(pronunciation.getCompletenessScore())
                    .pronunciationScore(pronunciation.getPronunciationScore())
                    .conversationEnded(false)
                    .nextScriptTurnId(nextTurn.getCoachingScriptTurnId())
                    .nextTurnOrder(nextTurn.getTurnOrder())
                    .nextAssistantText(nextTurn.getAssistantText())
                    .nextAssistantAudioUrl(nextTts.getAudioUrl())
                    .build();

        } catch (CoachingScriptTurnNotFoundException e) {
            return CoachingConversationDto.ResponseConversationTurn.builder()
                    .coachingSessionId(coachingSessionId)
                    .userMessageId(savedUserMessage.getCoachingMessageId())
                    .recognizedText(pronunciation.getRecognizedText())
                    .userFeedback(pronunciation.getFeedback())
                    .problemWords(pronunciation.getProblemWords())
                    .accuracyScore(pronunciation.getAccuracyScore())
                    .fluencyScore(pronunciation.getFluencyScore())
                    .completenessScore(pronunciation.getCompletenessScore())
                    .pronunciationScore(pronunciation.getPronunciationScore())
                    .conversationEnded(true)
                    .build();
        }
    }

    @Override
    @Transactional
    public CoachingConversationDto.ResponseFinishConversation finishConversation(
            Long coachingSessionId
    ) {
        CoachingSession session = getSession(coachingSessionId);

        CoachingMessageDto.ResponseGetCoachingMessages messagesResponse =
                coachingMessageService.getCoachingMessages(coachingSessionId);

        CoachingPronunciationResultDto.ResponseGetPronunciationResults pronunciationResponse =
                pronunciationResultService.getPronunciationResults(coachingSessionId);

        FastApiOpenAiDto.ResponseFinalFeedback finalFeedback =
                fastApiOpenAiClient.createFinalFeedback(
                        FastApiOpenAiDto.RequestFinalFeedback.builder()
                                .messages(toFastApiMessages(messagesResponse))
                                .pronunciationResults(toFastApiPronunciationResults(pronunciationResponse))
                                .build()
                );

        CoachingFeedbackDto.ResponseCoachingFeedback savedFeedback =
                coachingFeedbackService.saveCoachingFeedback(
                        CoachingFeedbackDto.RequestSaveCoachingFeedback.builder()
                                .coachingSessionId(coachingSessionId)
                                .totalScore(finalFeedback.getTotalScore())
                                .summaryFeedback(finalFeedback.getSummaryFeedback())
                                .naturalnessLevel(finalFeedback.getNaturalness().getLevel())
                                .naturalnessComment(finalFeedback.getNaturalness().getComment())
                                .flowLevel(finalFeedback.getFlow().getLevel())
                                .flowComment(finalFeedback.getFlow().getComment())
                                .pronunciationLevel(finalFeedback.getPronunciation().getLevel())
                                .pronunciationComment(finalFeedback.getPronunciation().getComment())
                                .problemWords(toJson(finalFeedback.getPronunciation().getProblemWords()))
                                .build()
                );

        session.complete();

        FastApiOpenAiDto.ResponseYoutubeKeywords keywords =
                fastApiOpenAiClient.createYoutubeKeywords(
                        FastApiOpenAiDto.RequestYoutubeKeywords.builder()
                                .finalFeedback(finalFeedback.getSummaryFeedback())
                                .build()
                );

        if (keywords.getKeywords() != null && !keywords.getKeywords().isEmpty()) {
            String keyword = keywords.getKeywords().get(0);

            FastApiYoutubeDto.ResponseYoutubeSearch youtubeResponse =
                    fastApiYoutubeClient.searchYoutube(
                            FastApiYoutubeDto.RequestYoutubeSearch.builder()
                                    .keyword(keyword)
                                    .maxResults(3)
                                    .build()
                    );

            contentService.saveAll(
                    coachingSessionId,
                    keyword,
                    youtubeResponse.getYoutubePicks()
            );
        }

        ContentDto.ResponseGetContents contents =
                contentService.getContents(coachingSessionId);

        return CoachingConversationDto.ResponseFinishConversation.builder()
                .feedback(savedFeedback)
                .pronunciationResults(pronunciationResponse)
                .contents(contents)
                .build();
    }

    private CoachingSession getSession(Long coachingSessionId) {
        return coachingSessionRepository.findById(coachingSessionId)
                .orElseThrow(CoachingSessionNotFoundException::new);
    }

    private List<CoachingScriptTurnDto.RequestSaveCoachingScriptTurn> convertMessagesToScriptTurns(
            Long coachingSessionId,
            List<FastApiOpenAiDto.MessageItem> messages
    ) {
        List<CoachingScriptTurnDto.RequestSaveCoachingScriptTurn> turns = new ArrayList<>();

        int turnOrder = 1;

        for (int i = 0; i < messages.size(); i++) {
            FastApiOpenAiDto.MessageItem assistant = messages.get(i);

            if (assistant.getRole() != CoachingMessageRole.ASSISTANT) {
                continue;
            }

            String expectedText = null;

            if (i + 1 < messages.size()
                    && messages.get(i + 1).getRole() == CoachingMessageRole.USER) {
                expectedText = messages.get(i + 1).getMessage();
            }

            turns.add(
                    CoachingScriptTurnDto.RequestSaveCoachingScriptTurn.builder()
                            .coachingSessionId(coachingSessionId)
                            .turnOrder(turnOrder)
                            .assistantText(assistant.getMessage())
                            .expectedText(expectedText)
                            .build()
            );

            turnOrder++;
        }

        return turns;
    }

    private List<FastApiOpenAiDto.PreviousMessage> toFastApiMessages(
            CoachingMessageDto.ResponseGetCoachingMessages messagesResponse
    ) {
        return messagesResponse.getMessages()
                .stream()
                .map(message -> FastApiOpenAiDto.PreviousMessage.builder()
                        .role(message.getRole())
                        .message(message.getMessage())
                        .build())
                .toList();
    }

    private List<FastApiOpenAiDto.PronunciationResult> toFastApiPronunciationResults(
            CoachingPronunciationResultDto.ResponseGetPronunciationResults pronunciationResponse
    ) {
        return pronunciationResponse.getPronunciationResults()
                .stream()
                .map(result -> FastApiOpenAiDto.PronunciationResult.builder()
                        .expectedText(result.getExpectedText())
                        .recognizedText(result.getRecognizedText())
                        .accuracyScore(result.getAccuracyScore())
                        .fluencyScore(result.getFluencyScore())
                        .completenessScore(result.getCompletenessScore())
                        .pronunciationScore(result.getPronunciationScore())
                        .problemWords(parseProblemWordNames(result.getProblemWords()))
                        .build())
                .toList();
    }

    private List<String> parseProblemWordNames(String problemWordsJson) {
        if (problemWordsJson == null || problemWordsJson.isBlank()) {
            return List.of();
        }

        try {
            List<?> rawList = objectMapper.readValue(
                    problemWordsJson,
                    List.class
            );

            return rawList.stream()
                    .map(item -> {
                        if (item instanceof String word) {
                            return word;
                        }

                        if (item instanceof java.util.Map<?, ?> map) {
                            Object word = map.get("word");
                            return word != null ? word.toString() : null;
                        }

                        return null;
                    })
                    .filter(word -> word != null && !word.isBlank())
                    .toList();

        } catch (JsonProcessingException e) {
            return List.of();
        }
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}