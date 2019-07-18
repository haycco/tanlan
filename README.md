# Tanlan
Tanlan is webflux reactive micro-services for Spring Cloud 2.x Application, which is a higher speed higher reactive streams micro-services cluster build for reactor-netty、reactive mongodb、reactive redis.

[![LICENSE](https://img.shields.io/badge/license-Anti%20996-blue.svg?style=flat-square)](https://github.com/996icu/996.ICU/blob/master/LICENSE)

## Project Structure
![Image](https://raw.githubusercontent.com/haycco/imgrepo/master/img/project-struct.png)

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
![Image](https://raw.githubusercontent.com/haycco/imgrepo/master/img/registry-server.png)
5. Next step Start `config-server` micro-service project
6. Point your browser to [https://localhost:8888](https://localhost:8888) user&password: admin
7. Start `monitor-server` micro-service project
8. Point your browser to [https://localhost:8000](https://localhost:8000)
![Image](https://raw.githubusercontent.com/haycco/imgrepo/master/img/monitor.png)
9. Point your browser to [https://localhost:8080/swagger-ui.html](https://localhost:8080/swagger-ui.html) auth server user&password: admin
![Image](https://raw.githubusercontent.com/haycco/imgrepo/master/img/swagger-api.png)

## License
[Anti-996 License](LICENSE)

 - The purpose of this license is to prevent anti-labour-law companies from using the software or codes under the license, and force those companies to weigh their way of working
 - This draft is adapted from the MIT license. For a more detailed explanation, please see [Wiki](https://github.com/kattgu7/996-License-Draft/wiki). This license is designed to be compatible with all major open source licenses.  
 
## Contact
You can reach me by [E-mail](mailto:haycco@gmail.com) if you need.