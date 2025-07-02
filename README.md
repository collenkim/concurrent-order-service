# 🛒 쇼핑몰 주문 처리 시스템

> **실시간 대량 주문 요청 환경에서의 안정적인 주문 처리 시스템 구현**  
> Redis 분산 락, Kafka 메시지 처리, Spring MVC 비동기 처리 기반

---

## 📌 프로젝트 개요

본 프로젝트는 **프로모션, 타임세일, 한정 수량 판매** 등과 같이 **짧은 시간에 많은 주문 요청이 몰리는 쇼핑몰 환경**을 가정하여 설계되었습니다.

주요 과제는 다음과 같습니다:

- 중복 주문 방지
- 재고 초과 판매 방지
- 트랜잭션 충돌 방지
- 비동기 응답 처리
- 대량 트래픽 대응

---

## 🎯 핵심 해결 전략

- **Redis 기반 분산 락**  
  → 주문 중복 및 재고 차감 충돌 방지

- **Kafka 기반 비동기 메시지 처리**  
  → 주문 생성 이후 후속 작업 decoupling

- **Spring MVC + DeferredResult**  
  → 컨트롤러 레벨 비동기 응답 처리 (WAS 쓰레드 효율화)

- **CQRS 구조**  
  → 읽기/쓰기 책임 분리, 성능 최적화

- **도메인 기반 설계 및 관심사 분리**  
  → 유지보수성과 확장성 고려한 계층 구성

---


## 📦 기술 스택

| 구분 | 내용 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot (MVC), Spring Data JPA |
| Database | MariaDB |
| Cache/Lock | Redis (Redisson 사용) |
| Messaging | Apache Kafka |
| 비동기 처리 | DeferredResult, CompletableFuture |
| 컨테이너 환경 | Docker, Docker Compose |
| ORM | Hibernate (JPA) |
| 테스트 | JUnit5, TestContainers (선택적) |

---

## 🧱 아키텍처 구성

```text
Client
   ↓
API Controller
   ↓ (DeferredResult + CompletableFuture)
OrderFacade (비즈니스 흐름 제어)
   ↓
Redis Lock → OrderEntity 저장 (JPA)
   ↓
Kafka 메시지 발행 → 후속 처리