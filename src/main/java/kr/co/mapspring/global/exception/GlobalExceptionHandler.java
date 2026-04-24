package kr.co.mapspring.global.exception;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

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

    // 모든 예외 처리 (최종 fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Object>> handleException(Exception e) {

        return ResponseEntity.internalServerError()
                .body(ApiResponseDTO.fail(
                        org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR,
                        "서버 내부 오류가 발생했습니다."
                ));
    }
    
 // JSON 요청 바인딩 실패 처리
 // 예: birthDate에 "2000/01/01" 같은 잘못된 날짜 형식이 들어온 경우
 @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
 public ResponseEntity<ApiResponseDTO<Object>> handleHttpMessageNotReadableException(
         org.springframework.http.converter.HttpMessageNotReadableException e
 ) {
	 
     return ResponseEntity.badRequest()
             .body(ApiResponseDTO.fail(
                     org.springframework.http.HttpStatus.BAD_REQUEST,
                     "요청 데이터 형식이 올바르지 않습니다."
             ));
 }
}
