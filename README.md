# Mapingo Spring API Server

지도 기반 AI 영어 학습 서비스 Mapingo의 메인 백엔드 API 서버입니다.

React Frontend와 FastAPI AI Server 사이의 중심 서버 역할을 담당합니다.

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-6DB33F?style=for-the-badge&logo=springsecurity)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-Hibernate-59666C?style=for-the-badge&logo=hibernate)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql)
![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?style=for-the-badge&logo=redis)
![JWT](https://img.shields.io/badge/JWT-Authentication-black?style=for-the-badge&logo=jsonwebtokens)
![OAuth2](https://img.shields.io/badge/OAuth2-Google_Login-4285F4?style=for-the-badge&logo=google)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger)
![WebSocket](https://img.shields.io/badge/WebSocket-Realtime-010101?style=for-the-badge&logo=socketdotio)
![WebFlux](https://img.shields.io/badge/Spring_WebFlux-WebClient-6DB33F?style=for-the-badge&logo=spring)
![Gradle](https://img.shields.io/badge/Gradle-Build_Tool-02303A?style=for-the-badge&logo=gradle)
![Lombok](https://img.shields.io/badge/Lombok-Boilerplate_Reduction-BC4521?style=for-the-badge)
![PortOne](https://img.shields.io/badge/PortOne-Payment_API-FF6B6B?style=for-the-badge)
![Notion](https://img.shields.io/badge/Notion-Documentation-000000?style=for-the-badge&logo=notion)

---

## 주요 기능

- 로그인 기능
- 구독 기능
- 소셜 기능
- 즐겨찾기 기능
- 채팅 기능
- 학습 목표 기능
- 랭킹 기능
- AI 지도 학습 기능
- AI 음성 학습 기능
- 학습에 맞는 영상 추천 기능

---

## 팀원 및 역할

| 이름 | 역할 | 기능 구현 |
|---|---|---|
| 임수현 | 팀장 | (추가 예정) |
| 고은별 | 팀원 | // |
| 마은재 | 팀원 | // |
| 이가연 | 팀원 | // |
| 이현재 | 팀원 | // |
| 홍순찬 | 팀원 | // |

---

## 레포지토리 구조

| Repository | Description |
|---|---|
| language-react | React Frontend |
| language-spring | Spring Boot API Server |
| language-fastapi | FastAPI AI Server |

---

## 시스템 아키텍쳐

```text
Frontend
    ↓
Spring Boot API Server
    ↓
FastAPI AI Server
    ↓
OpenAI API, AZURE_SPEECH, YOUTUBE_API
```

---

## 개발 흐름

```text
feature branch 생성
    ↓
기능 개발
    ↓
commit
    ↓
pull request
    ↓
code review
    ↓
develop merge
```

---

## 협업 툴

- Notion
- GitHub
- Swagger
- Erd

---

## 프로젝트 규칙

- jpa랑 querydsl 분리할 것/컨트롤러 docs 파일 분리
- @Tag, @Operatoer, @Schema 어노테이션 사용하여 스웨거 문서화
- Controller에 작성하는 swagger문서는 docs로 분리
- 필요없는 어노테이션 지양
- 도메인 로직은 엔티티 내부에서 처리
- 서비스는 비즈니스 로직, 데이터 관련은 DTO (from, of  등 네이밍 규칙 맞춰서)
- API Url은 명사사용 (동사x)
- Entity → Res~DTO 변환은 setter보다는 builder 사용
- 작업하면서 추가 되거나 삭제되는 기능들은 노션에 기록
- 10. DTO 작성시 이너클래스는 Request+기능 ,Response+기능으로 코드 일치화하기, 
만약 request만 필요하더라도 이너클래스로 코드 스타일 통일화.

---

# 브랜치 운영

## 브랜치 구조

| 브랜치 | 상세 설명 |
|---|---|
| main | 운영 가능한 안정 버전 |
| feature/* | 기능 개발 브랜치 |

---

## 브랜치 네이밍 컨벤션

```bash
feature/login
feature/auth
feature/place
feature/chat
feature/admin
```

---

# 깃 컨벤션

## 커밋 규칙

```text
feat: 기능 추가
fix: 버그 수정
style: 코드 포맷/스타일 변경
chore: 기타 설정
design: UI/UX 변경
rename: 이름 변경
remove: 삭제
refactor: 리팩토링
build: 빌드 변경
```

---

# 기술 스택

## Backend

- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA
- JWT Authentication
- OAuth2 Client
- MySQL
- Redis
- WebSocket
- Spring WebFlux (WebClient)
- Swagger (SpringDoc OpenAPI)
- Gradle
- Lombok
- PortOne Payment API
- Notion


---

# 백엔드 컨벤션

## 패키지 구조

```text
kr.co.mapspring
├─ ai                
├─ chat              
├─ favorite        
├─ global          
├─ learning     
├─ payment          
├─ place            
├─ ranking           
├─ social            
├─ support           
├─ user             
├─ analysis        
├─ common          
├─ history        
├─ home            
└─ subscription     
```

---

## 네이밍 컨벤션

| 레이어 | 규칙 |
|---|---|
| Controller | ~Controller | UserController |
| Service | ~Service | UserService |
| Repository | ~Repository | UserRepository |
| DTO | ~Dto | LogionDto |
| Entity | 단수 명사 | User, Board |
| 응답 DTO | Response~Dto | ResponseLoginDto |
| 요청 DTO | Request~Dto | RequestLoginDto |
| Exception | ~Exception | LoginException |

---

## API 컨벤션

- REST API 기반 설계
- DTO 기반 Request / Response 처리
- Entity 직접 반환 금지
- 공통 응답 객체 사용

---

## 응답 컨벤션

```json
{
  "success": true,
  "message": "",
  "data": {}
}
```

---

## 예외 처리

- Global Exception Handler 사용
- ErrorCode Enum 기반 예외 관리
- Custom Exception 사용

---

# DB 협업

## ERD

[ERD 주소](https://www.erdcloud.com/d/Tvn5wazwsjCzRMzWh)

---

# Security

## Authentication

- JWT 기반 사용자 인증 처리
- Access Token 기반 로그인 유지
- Spring Security 기반 인증 처리
- OAuth2 기반 Google 로그인 지원
- 사용자 로그인 상태 검증 필터 적용

---

## Authorization

- USER / ADMIN 권한 분리
- Role 기반 접근 제어
- 인증 사용자 전용 API 분리
- 관리자 전용 API 권한 제한

---

# Environment Management

## Environment Variables

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=

FASTAPI_BASE_URL=

JWT_SECRET=

GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
OAUTH_GOOGLE_FAILURE_REDIRECT_URL=
OAUTH_GOOGLE_SUCCESS_REDIRECT_URL=
```

---

## Secret Rules

- .env 및 application-secret.yml 업로드 금지
- 환경 변수는 로컬에서 개별 관리
- 민감 정보 GitHub 업로드 금지

---

# Run Project

## Clone Repository

```bash
git clone [[repository-url](https://github.com/soo97/languagemap-react.git)]
```

---

## Run Spring Boot

```bash
./gradlew bootRun
```

---

## Swagger

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Trouble Shooting

---
