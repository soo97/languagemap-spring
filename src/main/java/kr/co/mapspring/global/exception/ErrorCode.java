package kr.co.mapspring.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "데이터를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 존재하는 데이터입니다."),
    
    //User 로그인용
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INACTIVE_USER(HttpStatus.FORBIDDEN, "비활성 사용자입니다."),
	
	//User 회원가입용
	EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
	PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    // Favorite Place
    FAVORITE_PLACE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 즐겨찾기한 장소입니다."),
    FAVORITE_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 즐겨찾기 장소입니다."),

    // Favorite Scenario
    FAVORITE_SCENARIO_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 즐겨찾기한 시나리오입니다."),
    FAVORITE_SCENARIO_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 즐겨찾기 시나리오입니다."),

    // Learning Goal
    GOAL_MASTER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 학습 목표입니다."),
    GOAL_ALREADY_SELECTED(HttpStatus.CONFLICT, "이미 선택한 학습 목표입니다."),
    GOAL_SELECTION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "학습 목표는 최대 3개까지만 선택할 수 있습니다."),
    USER_GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 목표입니다."),

    // Ranking
    RANKING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 랭킹이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
