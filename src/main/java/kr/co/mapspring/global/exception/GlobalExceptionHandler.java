package kr.co.mapspring.global.exception;

import java.util.HashMap;
import java.util.Map;

import kr.co.mapspring.global.exception.learning.GoalAlreadySelectedException;
import kr.co.mapspring.global.exception.learning.GoalMasterNotFoundException;
import kr.co.mapspring.global.exception.learning.GoalSelectionLimitExceededException;
import kr.co.mapspring.global.exception.learning.UserGoalNotFoundException;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

import kr.co.mapspring.global.dto.ApiResponseDTO;

@Slf4j
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleDataIntegrityViolationException(
            DataIntegrityViolationException e
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponseDTO.fail(
                        HttpStatus.CONFLICT,
                        "사용 중인 학습 목표는 삭제할 수 없습니다. 비활성화를 이용해주세요."
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

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleJwtException(JwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDTO.fail(
                        HttpStatus.UNAUTHORIZED,
                        "인증이 만료되었거나 유효하지 않습니다."
                ));
    }

    // 모든 예외 처리 (최종 fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleException(Exception e) {

        log.error("Unhandled exception occurred", e);

        return ResponseEntity.internalServerError()
                .body(ApiResponseDTO.fail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류가 발생했습니다."
                ));
    }
}
