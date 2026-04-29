# JWT + Redis Refresh Token 사용 가이드


Spring Boot 실행 전 환경변수 설정 필수적으로 하셔야 서버 실행이 가능합니다

Name = JWT_SECRET
Value = my-test-secret-key-must-be-at-least-32-characters



현재는 팀원 작업 보호를 위해 permitAll 상태이며,
추후 authenticated 방식으로 전환 예정

## 1. 현재 구현 상태

현재 로그인 성공 시 서버는 JWT 기반 토큰을 발급합니다.

발급되는 토큰은 두 가지입니다.

- Access Token
- Refresh Token

로그인 성공 응답에는 사용자 정보와 함께 `accessToken`, `refreshToken`이 포함됩니다.

Refresh Token은 응답으로 내려가는 동시에 Redis에도 저장됩니다.

Redis 저장 형식은 다음과 같습니다.

```text
key   = refresh:{userId}
value = refreshToken
TTL   = 7일

```

## 2. 현재 인증 적용 방식

현재는 팀원들의 기존 API 작업과 테스트에 영향을 주지 않기 위해 JWT 인증을 강제하지 않습니다.

- 즉, 현재 설정은 다음과 같습니다.

토큰이 있는 요청   → JWT 필터가 토큰을 읽고 인증 정보를 세팅
토큰이 없는 요청   → 기존처럼 요청 허용
잘못된 토큰 요청   → 인증 정보 없이 요청 통과

- 추후 기능이 정리되면 아래 방식으로 변경할 예정입니다.

/api/auth/login    → 인증 없이 허용
/api/auth/signup   → 인증 없이 허용
Swagger 관련 경로  → 인증 없이 허용
그 외 API          → JWT Access Token 필요


## 3. Docker Redis 실행 방법

Redis는 Docker를 통해 실행합니다.

프로젝트 루트에 있는 docker-compose.yml을 기준으로 실행합니다.

docker compose up -d


실행 확인:

docker ps

정상 실행 시 map-spring-redis 컨테이너가 보여야 합니다.

## 4. Redis 접속 확인

Redis CLI에 접속합니다

docker exec -it map-spring-redis redis-cli

접속 후 아래 명령어를 입력합니다.

ping

정상 응답:

PONG

## 5. Spring Boot 실행 전 환경변수 설정


JWT secret은 환경변수로 관리합니다.

STS에서 실행하는 경우:

Run Configurations
→ Environment
→ Add

아래 값을 추가합니다.

Name  = JWT_SECRET
Value = my-test-secret-key-must-be-at-least-32-characters


기본값은 다음과 같습니다.

Access Token  = 1시간
Refresh Token = 7일


## 6. 로그인 API 테스트 방법


Swagger 또는 Postman에서 로그인 API를 호출합니다.

POST /api/auth/login

요청 예시:

{
  "email": "test@naver.com",
  "password": "1234"
}

응답 예시:

{
  "success": true,
  "status": 200,
  "message": "로그인 성공",
  "data": {
    "userId": 1,
    "email": "test@naver.com",
    "name": "홍길동",
    "role": "USER",
    "accessToken": "<ACCESS_TOKEN>",
    "refreshToken": "<REFRESH_TOKEN>"
  }
}

주의: 실제 토큰 값은 문서나 단체 채팅에 공유하지 않습니다.


## 7. Redis에 Refresh Token 저장 확인

로그인 성공 후 Redis CLI에서 아래 명령어를 실행합니다.

keys refresh:*

예상 결과:

1) "refresh:1"

저장된 Refresh Token 확인:

get refresh:1

TTL 확인:

ttl refresh:1

7일 설정이면 약 604800초에 가까운 값이 나옵니다.

## 8. Access Token 사용 방법

현재는 인증 강제 상태가 아니므로 토큰이 없어도 API 요청이 막히지는 않습니다.

다만 토큰을 사용하는 경우 요청 Header에 아래 값을 넣으면 됩니다.

Authorization: Bearer <ACCESS_TOKEN>

추후 JWT 인증 강제 적용 시에는 로그인/회원가입/Swagger를 제외한 API 요청에 이 Header가 필요합니다.