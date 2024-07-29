![header](https://capsule-render.vercel.app/api?type=wave&color=FFABAB&height=300&section=header&text=YOUGOD❤️&fontSize=90&fontColor=FFFFFF&animation=fadeIn&stroke=2&strokeColor=FFFFFF)

# 쇼핑몰 서버 API

이 프로젝트는 쇼핑몰 웹 애플리케이션을 위한 서버 API를 제공합니다. 
이 API는 제품 관리, 주문 처리, 사용자 인증 및 결제 처리 등 쇼핑몰 운영에 필요한 주요 기능을 포함합니다.

## 소개

쇼핑몰 서버 API는 쇼핑몰 웹 애플리케이션의 백엔드 시스템을 구성하는 API입니다. 
이 API는 다양한 쇼핑몰 기능을 지원하여 개발자들이 빠르고 효율적으로 쇼핑몰 플랫폼을 구축할 수 있도록 돕습니다. 
주요 기능으로는 상품 목록 관리, 장바구니 처리, 주문 생성 및 결제, 사용자 인증 및 관리 등이 있습니다.

## 해결하는 문제

- **상품 관리**: 상품 정보를 추가, 수정, 삭제하고, 상품 목록을 조회할 수 있는 기능을 제공합니다.
- **장바구니 처리**: 사용자가 장바구니에 상품을 추가하거나 제거하고, 장바구니 내용을 조회할 수 있는 기능을 제공합니다.
- **주문 처리**: 사용자가 주문을 생성하고, 주문 상태를 조회 및 업데이트할 수 있는 기능을 제공합니다.
- **결제 처리**: 주문에 대한 결제를 처리하고, 결제 상태를 관리할 수 있는 기능을 제공합니다.
- **사용자 인증**: 사용자 등록, 로그인, 로그아웃 및 사용자 정보 관리를 지원합니다.
- **이메일을 통한 비밀번호 변경**: 사용자가 비밀번호를 잊어버렸을 때 이메일을 통해 비밀번호를 재설정할 수 있는 기능을 제공합니다.
- **리프레시 토큰**: 리프레시 토큰을 사용하여 사용자 세션을 유지하고, 액세스 토큰이 만료된 경우 새로 고침할 수 있는 기능을 제공합니다.


## 기능

- **주소 API**
  - POST /addresses - 새로운 주소 저장
  - GET /addresses/{memberId} - 특정 회원의 모든 주소 목록 조회
  - PUT /addresses/{id} - 주소 정보 수정
  - DELETE /addresses/{id} - 주소 삭제

- **관리자 API**
  - POST /admins/signup - 관리자 회원가입
  - POST /admins/login - 관리자 로그인
  - DELETE /admins/logout - 관리자 로그아웃
  - POST /admins/refreshToken - 관리자 토큰 갱신

- **장바구니 API**
  - POST /carts - 새로운 장바구니 생성
  - GET /carts - 특정 회원의 장바구니 조회

- **결제 API**
  - POST `/payments` - 결제 처리
  - GET `/payments/{id}` - 결제 상태 조회

- **장바구니 아이템 API**
  - POST /cartItems - 장바구니에 상품 추가
  - GET /cartItems - 특정 장바구니의 아이템 조회
  - PUT /cartItems/{itemId} - 장바구니 아이템 수량 업데이트
  - DELETE /cartItems/deleteAfterPayment - 결제 후 장바구니 아이템 삭제
  - DELETE /cartItems/cancelCartItems - 장바구니 아이템 취소
 
- **카테고리 API**
  - POST /categories - 새로운 카테고리 추가 (관리자 권한 필요)
  - GET /categories - 모든 카테고리 조회
 
- **회원 API**
  - POST /members/signup - 회원가입
  - POST /members/login - 로그인
  - DELETE /members/logout - 로그아웃
  - POST /members/refreshToken - 토큰 갱신
  - GET /members/info - 회원 정보 조회
  - POST /members/findpassword - 비밀번호 찾기 이메일 전송
  - POST /members/resetpassword - 비밀번호 재설정
  - GET /members/resettoken - 비밀번호 리셋 토큰 유효성 검토
  - PUT /members/update/{id} - 회원 정보 수정
  - DELETE /members/delete/{id} - 회원 삭제
 
- **결제 API**
  - GET /paymentInfos/{impUid} - 결제 정보 확인
  - GET /paymentInfos - 모든 결제 정보 조회
 
- **상품 API**
  - POST /products - 새로운 상품 추가 (관리자 권한 필요)
  - GET /products - 상품 목록 조회
  - GET /products/{id} - 특정 상품 정보 조회

## 설치 방법

이 프로젝트는 Gradle을 사용하여 관리되며, Java 17을 기반으로 합니다. 아래의 단계를 따라 설치 및 실행할 수 있습니다.

### 필수 조건

- [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) 이상이 설치되어 있어야 합니다.
- [Gradle](https://gradle.org/install/)이 설치되어 있어야 합니다.

### 프로젝트 클론

먼저, 이 프로젝트를 클론합니다:
```bash
git clone <프로젝트 레포지토리 URL>
cd <프로젝트 디렉토리>
```

# 종속성 설치
프로젝트 디렉토리에서 Gradle을 사용하여 필요한 종속성을 설치합니다.
```bash
./gradlew build
```
이 명령어는 프로젝트에 필요한 모든 종속성을 다운로드하고, 프로젝트를 빌드합니다.

# 데이터베이스 설정
src/main/resources/application.properties 또는 application.yml 파일에서 데이터베이스 설정을 구성합니다.
```bash
# key? 32byte (256bit)
jwt:
  secretKey: "secretKey" # 48 characters (48 * 8 = 384 bits)
  refreshKey: "refreshKey" # 48 characters (48 * 8 = 384 bits)

iamport:
  api:
    key: "iamport-apikey"
    secret: "iamport-secretkey"

spring:
  main:
    allow-bean-definition-overriding: true

  datasource:
    url: jdbc:mysql://<database-host>:<database-port>/<database-name>?useUnicode=true&serverTimezone=<timezone>
    username: <database-username>
    password: <database-password>

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        show_sql: true
        format_sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect

  mail:
    host: <smtp-host>
    port: <smtp-port>
    username: <smtp-username>
    password: <smtp-password>
    protocol: smtp
    default-encoding: UTF-8
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
    debug: true


```
# 애플리케이션 실행
애플리케이션을 실행하려면, 다음 명령어를 사용합니다.
```bash
./gradlew bootRun
```
이 명령어는 Spring Boot 애플리케이션을 실행합니다.

# 테스트 실행
프로젝트의 테스트를 실행하려면, 다음 명령어를 사용합니다.
```bash
./gradlew test
```
이 명령어는 모든 단위 테스트를 실행합니다.


## 깃허브 통계 

<div align="center">
  <img src="https://github-readme-stats.vercel.app/api?username=yousincha&show_icons=true&theme=radical" alt="YOUGOD's GitHub stats" />
  <img src="https://github-readme-stats.vercel.app/api/top-langs/?username=yousincha&layout=compact" alt="Top Langs" />
</div>

---

# 추가 설정
- application.properties 파일에서 데이터베이스 설정 및 애플리케이션 설정을 구성합니다.
- 애플리케이션 실행 전에 데이터베이스와 관련된 설정을 확인하고 필요에 따라 수정해 주세요.

