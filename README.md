# 🐢 닥터거북 (turtle5) — 거북목 자세 교정 서비스 · Backend

웹캠으로 사용자의 자세를 실시간으로 분석하다가, **거북목 자세가 감지되면 알람**을 보내고, **요일별 사용 시간과 알람 횟수를 통계로** 보여주는 서비스의 백엔드입니다.

> 한성대학교 캡스톤 디자인 · 팀 turtle5 (2024.03 ~ 2024.05)
> 🖥️ Frontend: [dain0l/capstone_turtlefive](https://github.com/dain0l/capstone_turtlefive)

## 🛠 Tech Stack

| 분류 | 기술 |
|---|---|
| Language / Framework | Java 17, Spring Boot 3.2 |
| Security / Auth | Spring Security 6, JWT (Access / Refresh Token) |
| Database | MySQL (AWS), Spring Data JPA |
| Cache | Redis (로그아웃 토큰 블랙리스트) |
| Posture Detection | MediaPipe |
| Build | Gradle |

## ✨ 주요 기능

### 회원 / 인증
- 회원가입, 아이디 중복 확인, 로그인, 로그아웃
- 아이디 찾기, 비밀번호 찾기·재설정
- 비밀번호는 `PasswordEncoder`로 해시해서 저장
- **JWT 인증**: 로그인하면 Access Token과 Refresh Token을 발급하고, `JwtAuthenticationFilter`로 요청마다 검증
- **로그아웃 처리**: 로그아웃한 토큰을 Redis 블랙리스트에 등록해 재사용을 막음

### 자세 로그
- 웹캠 사용 기록(시작·종료 시간) 저장
- 거북목 감지 알람 기록 저장

### 통계
- **이번 주 요일별 웹캠 사용 시간(분)과 알람 횟수** 집계 → 차트 데이터로 제공
- 사용 시간 대비 **바른 자세 비율** 계산
- 전체 이용자 수 조회

## 📡 API

| Method | Endpoint | 설명 |
|---|---|---|
| POST | `/api/signup` | 회원가입 |
| POST | `/api/signup/id_check` | 아이디 중복 확인 |
| POST | `/api/login` | 로그인 (JWT 발급) |
| POST | `/api/logout` | 로그아웃 (토큰 블랙리스트 등록) |
| POST | `/api/findID` | 아이디 찾기 |
| POST | `/api/findPassword` | 비밀번호 찾기 |
| POST | `/api/setPassword` | 비밀번호 재설정 |
| GET | `/api/mypage` | 내 정보 조회 |
| GET | `/api/Membercount` | 전체 이용자 수 |
| POST | `/api/webcam/log` | 웹캠 사용 기록 저장 |
| POST | `/api/webcam/alarmlog` | 거북목 알람 기록 저장 |
| GET | `/api/inquiry` | 요일별 사용 시간·알람 횟수 (차트) |
| GET | `/api/percentage` | 바른 자세 비율 |

## 📁 구조

```
capstone_back/demo/src/main/java/com/example/demo
├── api/          # REST 컨트롤러 (Client, Log, Chart, Error)
├── service/      # 비즈니스 로직 (회원, 인증, 로그, 차트 통계)
├── repository/   # JPA Repository
├── entity/       # Client, PW, WebCamLog, AlarmLog, Token 등
├── dto/          # 요청·응답 DTO
├── jwt/          # JwtTokenProvider, JwtAuthenticationFilter, Redis 설정
└── config/       # SecurityConfig, WebConfig (CORS)
```

## ▶️ 실행 방법

DB 접속 정보와 JWT 키는 **환경변수**로 넣어줘야 합니다.

```bash
export DB_URL="jdbc:mysql://<host>:3306/docturtleDB?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Seoul"
export DB_USERNAME="<username>"
export DB_PASSWORD="<password>"
export JWT_SECRET="<base64-encoded-256bit-key>"   # 예: openssl rand -base64 32
# 선택: REDIS_HOST (기본값 127.0.0.1), REDIS_PORT (기본값 6379)

cd capstone_back/demo
./gradlew bootRun
```

> Redis가 로컬에서 실행 중이어야 합니다 (`docker run -p 6379:6379 redis`).
