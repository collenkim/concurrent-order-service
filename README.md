# 주문 서비스 (Concurrent Order Service)

> 대량 트래픽 환경에서의 안정적인 주문 처리를 위한 CQRS 기반 주문 시스템입니다.

---

## 1. ✨ 프로그램 개요 및 주제 선정 이유

본 프로젝트는 다음과 같은 문제 해결을 목표로 설계되었습니다:

* 특정 이벤트(한정 수량, 할인 등)로 인해 수천 건의 동시 주문 요청이 들어오는 상황
* 재고 초과 판매, 중복 주문, DB 트랜잭션 충돌 등 실시간 동시성 문제 발생 가능

### ✔ 해결 목표

* Redis 기반 분산 락을 통한 중복 방지
* Kafka 기반 비동기 메시징 처리
* CompletableFuture 기반 비동기 API
* JPA 트랜잭션 관리로 데이터 정합성 보장

### 🌟 선정 이유

시니어 개발자로서 대규모 트래픽 환경에서 다음과 같은 기술을 통합적으로 적용해볼 수 있었기 때문입니다:

* 분산 락
* CQRS 패턴
* Kafka 메시징 처리
* Docker 기반 MSA 환경 세팅

---

## 2. 📊 전체 구조 및 설계 방향

### 🔹 시스템 구조 (CQRS 패턴 적용)

```
[ API Layer ]
└── OrderApi (DeferredResult, CompletableFuture 비동기 응답)
└── OrderAsyncService
└── OrderFacade
└── CommandService, QueryService

[ Domain Layer ]
└── Order, OrderItem 도메인 객체

[ Infrastructure Layer ]
└── RDS, Kafka, Redis 관련 Repository, Config

[ Kafka ]
└── 주문 생성 시 메시지 발행 (order.created)
```

### 📚 패키지 구조

```
concurrent.order.service
└── api                : REST API 진입점
└── application        : 서비스 로직, facade, async 처리
└── domain             : 도메인 모델 (Order, OrderItem)
└── infrastructure      : JPA, Kafka, Redis 구성
└── cd / constants     : 공통 상수, enum 등
```

### ✅ 설계 포인트

* **CQRS**: Command (주문 생성/취소) 와 Query (조회) 로직을 명확히 분리
* **JPA 트랜잭션**: `@Transactional` 명시적 선언으로 관리, 별도 AOP 설정 X
* **분산 락 처리**: Redisson 기반 `@DistributedLock` 어노테이션으로 중복 방지
* **비동기 처리**: `CompletableFuture + DeferredResult` 구조
* **Kafka 비동기 메시징**: 주문 완료 시 `order.created` 이벤트 발행

---

## 3. 🚀 실행 방법

### 🚚 Docker 실행

```bash
docker-compose up -d
```

* Redis: localhost:6379
* Kafka: localhost:9092 (Zookeeper 포함)
* MariaDB: localhost:3306

### 📆 애플리케이션 실행

```bash
./gradlew bootRun
```

> Swagger 또는 Postman 으로 다음 API 테스트 가능

### 🔧 주문 API 예시

```http
POST /api/v1/orders
{
  "userId":"test1234",
  "items":[
    {
      "productId":"PRD_240701120000001_0001_101",
      "quantity": 1,
      "productPrice": 10050
    }
  ],
  "discountPolicyId": null,
  "discountAmount": null,
  "deliveryFee": 0,
  "paymentAmount": 10050,
  "paymentType": "CREDIT"
}
```

---

## 4. 🧬 기술 스택

| 카테고리         | 기술                      |
| ------------ | ----------------------- |
| Language     | Java 17                 |
| Framework    | Spring Boot 3.x         |
| Build Tool   | Gradle                  |
| DB           | MariaDB (JPA)           |
| Cache / Lock | Redis + Redisson        |
| Messaging    | Apache Kafka            |
| Infra        | Docker / Docker Compose |

## 🔹 기타 참고 클래스 예시

* `OrderAsyncService`: 주문 등록/조회/취소의 비동기 처리
* `OrderFacade`: 도메인 조합 + command / query 처리 중심
* `OrderCommandService`, `OrderQueryService`: CQRS 구조 분리
* `OrderMapper`: Entity ↔ Domain ↔ DTO 변환 책임
* `KafkaOrderProducer`: Kafka 발행자
* `OrderCreatedEventDto`: Kafka 메시지 DTO
* `DeferredResultUtil`: CompletableFuture → DeferredResult 변환

---