package kr.co.mapspring.global.exception;

import java.util.HashMap;
import java.util.Map;

import kr.co.mapspring.global.exception.learning.GoalAlreadySelectedException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.global.exception.learning.GoalSelectionLimitExceededException;
import kr.co.mapspring.global.exception.learning.UserGoalNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import kr.co.mapspring.global.exception.ai.LearningSessionNotFoundException;
import kr.co.mapspring.global.exception.ai.AssistantMessageRequiredException;
import kr.co.mapspring.global.exception.ai.CoachingMessageRoleRequiredException;
import kr.co.mapspring.global.exception.ai.CoachingSessionNotFoundException;

import kr.co.mapspring.global.dto.ApiResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleCustomException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponseDTO.fail(errorCode.getStatus(), e.getMessage()));
    }
    
    // AI 코칭 리소스 없음(LearningSession) 예외 처리
    @ExceptionHandler(LearningSessionNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleLearningSessionNotFoundException(
            LearningSessionNotFoundException e
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.fail(
                        HttpStatus.NOT_FOUND,
                        e.getMessage()
                ));
    }
    
    // AI 코칭 코칭 세션(CoachingSession) 없음 예외 처리
    @ExceptionHandler(CoachingSessionNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleCoachingSessionNotFoundException(
            CoachingSessionNotFoundException e
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.fail(
                        HttpStatus.NOT_FOUND,
                        e.getMessage()
                ));
    }

    // AI 코칭 메시지 요청값 예외 처리
    @ExceptionHandler({
            CoachingMessageRoleRequiredException.class,
            AssistantMessageRequiredException.class
    })
    public ResponseEntity<ApiResponseDTO<Object>> handleInvalidCoachingMessageException(
            RuntimeException e
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.fail(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage()
                ));
    }

    // Learning 목표 관련 예외 처리
    @ExceptionHandler({
            GoalSelectionLimitExceededException.class
    })
    public ResponseEntity<ApiResponseDTO<Object>> handleLearningBadRequestException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.fail(
                        HttpStatus.BAD_REQUEST,
                        e.getMessage()
                ));
    }

    @ExceptionHandler({
            GoalMasterNotFoundException.class,
            UserGoalNotFoundException.class
    })
    public ResponseEntity<ApiResponseDTO<Object>> handleLearningNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.fail(
                        HttpStatus.NOT_FOUND,
                        e.getMessage()
                ));
    }

    @ExceptionHandler(GoalAlreadySelectedException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleGoalAlreadySelectedException(
            GoalAlreadySelectedException e
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponseDTO.fail(
                        HttpStatus.CONFLICT,
                        e.getMessage()
                ));
    }

    // @Valid 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleValidationException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(ApiResponseDTO.fail(
                        org.springframework.http.HttpStatus.BAD_REQUEST,
                        "입력값 검증 실패",
                        errors
                ));
    }
    
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMessageConversionException.class})
    public ResponseEntity<ApiResponseDTO<Object>> handleRequestBodyParseException(Exception e) {
        return ResponseEntity.badRequest()
                .body(ApiResponseDTO.fail(
                        HttpStatus.BAD_REQUEST,
                        "입력값 검증 실패"
                ));
    }

    // 모든 예외 처리 (최종 fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleException(Exception e) {

        return ResponseEntity.internalServerError()
                .body(ApiResponseDTO.fail(
                        org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류가 발생했습니다."
                ));
    }
}