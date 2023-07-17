# _Directors_
![di_logo](https://github.com/f-lab-edu/directors/assets/76680394/6580c1fd-efb8-448f-93d5-649717b5e654)

## 📌프로젝트 개요
- 지역 기반으로 취준생들과 경력 있는 전문가들 간의 인적 네트워크를 중개하는 플랫폼 서비스
  
- 이 서비스를 통해 취준생들은 자신의 직업과 직무에 대한 궁금한 점을 1:1 대면으로 해결할 수 있고, 경력 있는 전문가들은 자신의 지식과 경험을 나누며 멘토링의 역할을 수행할 수 있습니다.
<br>


## ✅ 사용 기술 및 개발 환경
Java, Spring Boot, Jpa, Gradle, MySQL, QueryDsl, Redis, Junit, Naver Cloud, Github Action, Docker, Nodejs


## 🖥️ https://www.directors.run (접속 가능)
### 현재 프론트엔드 코드를 작성 중에 있습니다.
- 위 URL를 통해 현재 프론트 작업 내용을 확인하실 수 있습니다.
- Front Github: https://github.com/tilsong/directors_front
- 프론트는 vue.js를 사용하여 개발했습니다.

<br>


## 📰 기술적 issue 

### 지역 인증 프로세스 만들기
- 한국의 행정 구역을 기반으로 유저의 지역을 인증하는 기능을 개발했습니다.
  - [➡️ 해당 과정이 담긴 Pr Link](https://github.com/f-lab-edu/directors/pull/19)

### 의미 있는 테스트를 위한 데이터 만들기
- 성능 테스트 시 의미 있는 데이터를 사용하기 위해 크롤링 및 임의 데이터 생성 작업을 수행했습니다.
  - [➡️ 테스트 데이터를 만드는 Nodejs 코드를 담은 Github 링크](https://github.com/tilsong/Create-Test-Data)


### MySQL Spatial Index 및 범위 검색 쿼리 적용을 통한 공간 데이터의 검색 성능 향상
- MySQL의 Spartial Index와 전용 쿼리를 적용하여 지역 검색 로직의 Latency를 기존 대비 20% 이하로 감소시켰습니다.
  - [➡️ 테스트 및 개선 과정이 담긴 블로그 링크](https://velog.io/@tilsong/%EB%B9%A0%EB%A5%B4%EA%B2%8C-%EC%A7%80%EC%97%AD-%EC%A1%B0%EA%B1%B4%EC%9C%BC%EB%A1%9C-%EA%B2%80%EC%83%89%ED%95%98%EA%B8%B0-R-Tree-%EC%9D%B8%EB%8D%B1%EC%8A%A4-%EC%A0%81%EC%9A%A9-%EB%B0%8F-%EC%BF%BC%EB%A6%AC-%EA%B0%9C%EC%84%A0)

### 코드 Push로 배포까지, Github Actions + Docker
- Github Action과 Docker와 Docker-compose를 사용하여 코드가 Main branch로 merge 되었을 경우, 지정된 서버에 애플리케이션이 배포되고 실행될 수 있도록 CD 파이프라인을 구축했습니다.
  - [➡️ 연동 경험이 담긴 블로그 링크](https://velog.io/@tilsong/%EC%BD%94%EB%93%9C-Push%EB%A1%9C-%EB%B0%B0%ED%8F%AC%EA%B9%8C%EC%A7%80-Github-Actions-Docker)
- Github Secret 트러블 슈팅 과정  [➡️ Github Secrets가 동작하지 않는다구요? 그건 아마도..](https://velog.io/@tilsong/Github-Secrets%EA%B0%80-%EB%8F%99%EC%9E%91%ED%95%98%EC%A7%80-%EC%95%8A%EB%8A%94%EB%8B%A4%EA%B5%AC%EC%9A%94-%EA%B7%B8%EA%B1%B4-%EC%95%84%EB%A7%88%EB%8F%84)

### Redis와 연동하여 sse 방식으로 채팅 기능 구현
- Redis의 Pubsub과 SSE 방식을 통해 채팅 기능을 구현했습니다. 
    - [➡️ 해당 과정이 담긴 Pr 링크](https://github.com/f-lab-edu/directors/pull/54)

## 기타
### 프로젝트 JPA 도입기 - 객체를 객체답게 사용하기
- 프로젝트 초기에는 자바의 컬렉션 프레임워크를 사용하여 데이터를 관리하다가, 이후 MySQL DB로 전환하게 되었습니다. 
  - [➡️ 전환 과정이 담긴 블로그 링크](https://velog.io/@tilsong/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%97%90-JPA-%EB%8F%84%EC%9E%85%ED%95%B4%EB%B3%B4%EA%B8%B0-1)

### JPA의 N+1, Fetch Join과 Limit를 함께 써서 발생하는 문제 해결하기
- 프로젝트에서 JPA를 사용하던 중 맞게 된 문제를 해결하는 과정을 담았습니다.
  - [➡️ 문제 해결 과정이 담긴 블로그 Link](https://velog.io/@tilsong/JPA%EB%A5%BC-%ED%86%B5%ED%95%9C-%EA%B2%80%EC%83%89-%EC%BF%BC%EB%A6%AC%EB%AC%B8-%EA%B0%9C%EC%84%A0%ED%95%98%EA%B8%B0-N1-%EB%AC%B8%EC%A0%9C-Fetch-Join%EA%B3%BC-Limit)