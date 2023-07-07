# sns

## 📌 개요

Instagram과 유사한 간단한 SNS 서비스 백엔드 구현<br>
~~도메인 주소: https://wlsh-sns.duckdns.org/ <br>~~
위키: https://github.com/wlsh44/sns/wiki<br>
프로젝트 소개 Url: https://garrulous-peony-f8e.notion.site/SNS-fbafe8ae31ab4dcc912b3772d0080676

## 📌 프로젝트 목표 및 중점 사항

- SNS 서비스 설계 및 구현
- JPA 활용과 N + 1 문제 경험 및 쿼리 튜닝을 통한 해결  
- Junit을 이용하여 높은 커버리지의 단위 테스트 작성을 통해 리팩터링 등의 과정에도 코드의 신뢰성 경험
- CI/CD를 적용하여 자동화 된 빌드와 무중단 배포 경험
- 성능 테스트을 경험하고, 개선을 통한 차이 확인
- 캐싱을 적용하여 과부화가 걸릴 것이라 판단되는 시나리오의 성능 개선(진행 중)

## 📌 사용 기술

**Idea**

![idea](https://img.shields.io/badge/IntelliJ-grey)

**Development**

![java](https://img.shields.io/badge/open--jdk-17-brightgreen)
[![framework](https://img.shields.io/badge/spring%20boot-2.7.8-yellowgreen)](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.7-Release-Notes)
[![build tool](https://img.shields.io/badge/gradle-7.6-orange)](https://gradle.org/)
![ORM](https://img.shields.io/badge/JPA-grey)
[![Rdb](https://img.shields.io/badge/MySQL-8.0-blue)](https://dev.mysql.com/doc/refman/8.0/en/)

**Deploy**

![cloud](https://img.shields.io/badge/Naver%20Cloud%20Platform-grey)
![OS](https://img.shields.io/badge/ubuntu-18.04-red)
![CI CD](https://img.shields.io/badge/Github%20Actions-grey)
[![image](https://img.shields.io/badge/docker-latest-lightgrey)](https://hub.docker.com/r/k87913j/sns)

**Test**

[![Rdb](https://img.shields.io/badge/JUnit-5.0-yellow)](https://junit.org/junit5/)
[![Rdb](https://img.shields.io/badge/mockito-5.3-yellowgreen)](https://site.mockito.org/)


**Load Test**

![load test](https://img.shields.io/badge/ngrinder-3.5-green)


## 📌 주요 기능

- 인증 및 인가 기능
- 게시글 기능
- 댓글 기능
- 팔로우 기능
- 좋아요 기능
- 알람 기능
- 피드 기능

## 📌 아키텍쳐

<img width="873" alt="image" src="https://github.com/wlsh44/sns/assets/49276666/a496bb73-b8ce-4bee-b399-354ed67d6302">


## 📌 브랜치 및 커밋 전략

**Pull Request**

- PR 전략
  - Squash and Merge
- PR 커밋 메시지
  - Default to pull request title and commit details
- develop, main 브랜치 커밋의 수를 줄여, 전체적인 개발 흐름의 파악을 위함

**Commit Convention**

- \<type>: \<body>
- type
  - feat: 새로운 기능 추가
  - modify: API 변경 등의 기능 변경
  - refactor: 코드 리팩터링
  - fix: 버그 수정
  - chore: 코드 외의 작업
  - style: 코드 포맷팅 및 정리
  - test: 테스트 관련 코드 변경 및 추가

**Branch**

- main: 배포를 위한 브랜치
- develop: 다음 버전 개발을 위한 브랜치
- 이 외의 브랜치는 브랜치의 개발 기능을 중점으로 커밋 컨벤션의 type과 유사하게 함
  - feature 브랜치를 제외하고 type/issue번호 의 이름 전략을 사용함
