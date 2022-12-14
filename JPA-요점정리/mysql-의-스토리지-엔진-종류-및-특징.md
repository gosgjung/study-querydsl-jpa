# mysql 의 스토리지 엔진 종류 및 특징

<br>

# 참고자료

오늘 정리하는 내용은 감사하게도 아래 링크에서 많은 내용을 공부했다. 

- [mysql 스토리지 엔진 종류 및 특징](https://nomadlee.com/mysql-%EC%8A%A4%ED%86%A0%EB%A6%AC%EC%A7%80-%EC%97%94%EC%A7%84-%EC%A2%85%EB%A5%98-%EB%B0%8F-%ED%8A%B9%EC%A7%95/)
- [dev.mysql.com](https://dev.mysql.com/doc/refman/8.0/en/pluggable-storage-overview.html)

<br>

# MySQL 의 스토리지 엔진 종류들

MySQL 에는 여러가지 종류들이 있다.

- InnoDB 엔진
- MyISAM 엔진
- Memory 엔진
- Archive 엔진
- CSV 엔진
- Federated 엔진

<br>

MySQL의 스토리지 엔진들은 종류가 많다. 아래의 그림을 보면 가장 아래층에 위치한 계층에 `InnoDB`, `MyISAM`, `NDB`, `MEMORY` 등이 표현되어있는 것을 볼 수 있다.

> 이미지 출처 : [dev.mysql.com](https://dev.mysql.com/doc/refman/8.0/en/pluggable-storage-overview.html)

<br>

![1](./img/MYSQL-STORAGE-ENGUINE/mysql-architecture.png)

<br>

오늘 요약할 스토리지 엔진들은 `InnoDB엔진`, `MyISAM 엔진`, `Memory 엔진` 이다.

<br>

# InnoDB 엔진

`InnoDB` 는 가장 많이 사용되는 Storage Engine 이다. `InnoDB` 는 `ACID` 트랜잭션을 지원하는 엔진이다. <br>

MySQL의 대표적인 엔진이기도 하고, 성능 역시 뛰어나고, 장애 복구 기능을 가지고 있는 엔진이다.

트랜잭션을 처리하기 위해 고안되었다고 한다. 대부분의 경우 롤백되지 않고 정상적으로 종료되는 짧은 트랜잭션이 많은 경우를 처리하기에 유리하게끔 되어있는 엔진이다.<br>

결제 정보와 같은 정보의 무결성을 가져야하고 손실되면 안되는 중요한 데이터를 필요로 할 때 사용한다.<br>

InnoDB의 주요 특징은 아래와 같다.

- Primary Key 기반 클러스터링
- MVCC 지원
- 외래키 지원
- 자동화된 장애복구
- 오라클 아키텍처와 일부 유사

<br>

InnoDB의 주요한 특징들을 정리해보면 아래와 같다.<br>

- 프라이머리 키를 기준으로 클러스터링
  - 모든 테이블을 기본적으로 프라이머리 키를 기준으로 클러스터링해서 저장
  - 프라이머리 키를 기준으로 클러스터링 되었기에 프라이머리 키에 의한 Range 스캔이 빠른편이다.
  - MySQL의 다른 대부분의 스토리지 엔진에서 사용하는 인덱스 구조와는 다르게 기본키 조회가 빠르게 되도록 설계되어 있다.
- MVCC 지원 - 락 없는 일관된 읽기
  - `MVCC(Multi Version Concurrency Control)` 를 통해 락을 걸지 않고 읽기 작업을 수행한다.
  - 락을 걸지 않기 때문에 읽기 작업시 트랜잭션의 락을 기다리지 않아도 된다는 장점이 있다.
- 외래키 지원
  - InnoDB 스토리지 엔진 레벨에서 지원하는 기능이다.
  - 엄청 중요한 기능은 아니지만, MyISAM, MEMORY 엔진 기반 테이블에서는 사용하지 못하는 기능이다.
  - 외래키의 경우 운영환경에서는 여러가지 제약사항이 많기에 가급적 외래키를 걸어두지는 않는 편이다.
  - 다만, 개발환경에서 외래키를 사용한다면 유용할 수도 있겠다.
- 자동 데드락 감지
  - 그래프 기반의 데드락 체크방식을 사용한다.
  - 데드락이 발생함과 동시에 바로 감지된다. 데드락이 감지되면, 관련된 트랜잭션 들 중 rollback이 가능한 트랜잭션들을 자동으로 강제 종료한다.
  - 이런 이유로 데드락으로 인한 쿼리의 타임아웃,슬로우 쿼리가 기록되는 경우는 흔치않다.

- 자동화된 장애 복구
- 오라클DB와 유사한 아키텍처 일부 수용
  - MVCC 기능이 제공된다는 점, 언두 데이터의 시스템 테이블 스페이스에서의 관리, 테이블 스페이스 개념 등은 오라클 아키텍처와 유사한 면이 있다.

<br>

# MyISAM 엔진

MyISAM 은 Transaction을 지원하지 않는다. InnoDB보다 간단하고 기본적으로 빠르다. 하지만, 동시성 제어가 어렵다. Row(행) 수준의 잠금을 지원하지 않고, Table lock 을 제공한다. 또한 트랜잭션 역시 제공하지 않는다.<br>

이런 이유로 Read 쿼리가 많은 DW환경에서 자주 사용된다.<br>

MyISAM은 full-text 인덱싱, 압축, 공간(GIS) 관련 함수 등 유용한 기능을 제공하기도 한다.<br>

세부적으로 아래의 특징들이 존재한다.

- Key Cache
  - InnoDB의 버퍼풀과 유사한 역할을 한다. 
  - Key Cache 는 인덱스만을 대상으로 작동한다. 단 이 인덱스도 디스크 쓰기 작업에 대해서만 부분적으로 버퍼링 역할만 한다.
- 테이블 레벨의 잠금과 동시성
  - MyISAM은 행(row)단위의 테이블 레벨의 락을 제공한다.
- 수동복구
  - Check table [테이블 명] 또는 Repair Table [테이블 명] 이라는 명령을 통해 테이블 오류를 조사하거나 복구하는 것이 가능하다.

<br>

# Memory 엔진

메모리에 데이터를 저장하는 엔진이다. 트랜재겻니 지원되지 않고, `table level locking` 을 제공한다.<br>

메모리를 사용하기에 기본적으로 속도가 빠른 편이지만, 데이터를 일어버릴 위험이 있다. 속도가 빠른 점으로 인해 임시 테이블로 많이 사용되는 편이다. <br>

<br>

# 그 외에

Archive 엔진, CSV 엔진, Federated 엔진이 있다.<br>

<br>
