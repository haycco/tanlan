# Tanlan
Tanlan is webflux reactive micro-services for Spring Cloud 2.x Application, which is a higher speed higher reactive streams micro-services cluster build for reactor-netty、reactive mongodb、reactive redis.

## Requirements
* JDK 1.8+
* Maven 3+
* Docker&docker-compose
* [Reactor 3](https://projectreactor.io/docs/core/release/reference/)
* [Spring Boot 2.x (2.1.6.RELEASE)](https://spring.io/projects/spring-boot)
* [Spring Cloud 2.x (Greenwich.SR1)](https://spring.io/projects/spring-cloud)
* [Redis 4.0+](https://redis.io/documentation)
* [RocketMQ 4.4.0+](https://github.com/alibaba/spring-cloud-alibaba)
* [MongoDB 4.0+](https://docs.mongodb.com/manual/reference/sql-comparison/)
* [Zipkin](https://github.com/openzipkin/zipkin)
* [Spring Boot Admin (2.1.6)](https://github.com/codecentric/spring-boot-admin)

## Quick start
1. `mvn clean install`
2. `mvn spring-boot:run`
3. Start `registry-server` micro-service project
4. Point your browser to [https://localhost:8761](https://localhost:8761) user&password: admin
5. Next step Start `config-server` micro-service project
6. Point your browser to [https://localhost:8888](https://localhost:8888) user&password: admin
7. Start `monitor-server` micro-service project
8. Point your browser to [https://localhost:8000](https://localhost:8000)
