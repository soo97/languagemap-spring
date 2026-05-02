# Google OAuth 소셜 로그인 가이드

## 1. 개요
Google OAuth 로그인 성공 후 기존 JWT 구조를 재사용한다.

## 2. 로그인 시작 URL
GET http://localhost:8080/oauth2/authorization/google

## 3. Google Redirect URI
http://localhost:8080/login/oauth2/code/google

## 4. 필요 환경변수 셋팅
GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET
OAUTH_GOOGLE_SUCCESS_REDIRECT_URL
OAUTH_GOOGLE_FAILURE_REDIRECT_URL

## 5. 로컬 프론트 포트

http://localhost:80/oauth/success

## 6. DB 변경 (만약 안될시 건너 뛰어도 무방)
user.birth_date nullable
user.address nullable
user.phone_number nullable
user.password_hash nullable

## 7. 테스트 방법
1. Redis 실행
2. Spring Boot 실행
3. http://localhost:8080/oauth2/authorization/google 접속
4. Google 계정 선택
5. user, oauth_account, Redis refresh:* 확인

## 8. 주의사항
- accessToken/refreshToken은 URL에 노출되므로 캡처 공유 시 마스킹
- Query Parameter 전달 방식은 1차 개발용
- 운영 환경에서는 HttpOnly Cookie 방식 검토 필요