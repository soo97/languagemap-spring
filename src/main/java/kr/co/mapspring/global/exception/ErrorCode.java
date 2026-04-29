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
	PHONE_NUMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 전화번호입니다."),
	
	//User 약관동의용
	SERVICE_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "서비스 이용약관 동의는 필수입니다."),
	PRIVACY_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "개인정보 수집 및 이용 동의는 필수입니다."),
	TERMS_NOT_FOUND(HttpStatus.NOT_FOUND, "활성 약관 정보를 찾을 수 없습니다."),
	
	// JWT 토큰용
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다."),

    // Favorite Place
    FAVORITE_PLACE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 즐겨찾기한 장소입니다."),
    FAVORITE_PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 즐겨찾기 장소입니다."),

    // Favorite Scenario
    FAVORITE_SCENARIO_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 즐겨찾기한 시나리오입니다."),
    FAVORITE_SCENARIO_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 즐겨찾기 시나리오입니다."),

    // Friendship
    USER_NOT_FOUND_FOR_SOCIAL(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    SELF_FRIEND_REQUEST_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신에게 친구 요청을 보낼 수 없습니다."),
    FRIENDSHIP_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 친구 관계가 존재합니다."),
    FRIENDSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "친구 관계를 찾을 수 없습니다."),
    FRIEND_REQUEST_ACCESS_DENIED(HttpStatus.FORBIDDEN, "해당 친구 요청을 처리할 권한이 없습니다."),
  
    // Learning Goal
    GOAL_MASTER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 학습 목표입니다."),
    GOAL_ALREADY_SELECTED(HttpStatus.CONFLICT, "이미 선택한 학습 목표입니다."),
    GOAL_SELECTION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "학습 목표는 최대 3개까지만 선택할 수 있습니다."),
    USER_GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 목표입니다."),

    // Ranking
    RANKING_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 랭킹이 존재하지 않습니다."),
	
	// Admin Mission
	MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 미션입니다."),
	
	// Admin Place
	PLACE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 장소입니다."),
	PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 장소입니다."),
	
	// Admin Region
	REGION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 지역입니다."),
	REGION_IN_USE(HttpStatus.CONFLICT, "참조 중인 지역입니다."),
	
	// Admin Scenario
	SCENARIO_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 시나리오입니다."),
	SCENARIO_IN_USE(HttpStatus.CONFLICT, "참조 중인 시나리오입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
