# Simple Board with social login (kakao)

## application.yml

- clientId, clientSecret 값 할당할 것.

```yml
        kakao:
          clientId: ''
          clientSecret: ''
          clientAuthenticationMethod: client_secret_post
          authorizationGrantType: authorization_code
          redirectUri: http://localhost:8080/login/oauth2/code/kakao
          scope:
            - profile_nickname
            - profile_image
            - account_email
          clientName: Kakao
```

## docker

```shell
docker run -d --name=simple-board-mariadb --env=MYSQL_ROOT_PASSWORD=root -p 13341:3306 mariadb:11.2 &&
docker run -p 6379:6379 --name simple-board-redis -d redis:7.2 --requirepass "root"
```

## LIB
```
Gradle: ch.qos.logback:logback-classic:1.4.14
Gradle: ch.qos.logback:logback-core:1.4.14
Gradle: com.fasterxml.jackson.core:jackson-annotations:2.15.4
Gradle: com.fasterxml.jackson.core:jackson-core:2.15.4
Gradle: com.fasterxml.jackson.core:jackson-databind:2.15.4
Gradle: com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.4
Gradle: com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.15.4
Gradle: com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.4
Gradle: com.fasterxml.jackson.module:jackson-module-parameter-names:2.15.4
Gradle: com.fasterxml:classmate:1.6.0
Gradle: com.github.ben-manes.caffeine:caffeine:3.1.8
Gradle: com.github.gavlyukovskiy:datasource-decorator-spring-boot-autoconfigure:1.9.0
Gradle: com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0
Gradle: com.github.stephenc.jcip:jcip-annotations:1.0-1
Gradle: com.github.waffle:waffle-jna:3.3.0
Gradle: com.google.code.findbugs:jsr305:3.0.2
Gradle: com.google.errorprone:error_prone_annotations:2.21.1
Gradle: com.google.errorprone:error_prone_annotations:2.3.4
Gradle: com.google.guava:failureaccess:1.0.1
Gradle: com.google.guava:guava:28.2-jre
Gradle: com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava
Gradle: com.google.j2objc:j2objc-annotations:1.3
Gradle: com.jayway.jsonpath:json-path:2.9.0
Gradle: com.mysema.commons:mysema-commons-lang:0.2.4
Gradle: com.nimbusds:content-type:2.2
Gradle: com.nimbusds:lang-tag:1.7
Gradle: com.nimbusds:nimbus-jose-jwt:9.24.4
Gradle: com.nimbusds:oauth2-oidc-sdk:9.43.3
Gradle: com.querydsl:codegen-utils:5.0.0
Gradle: com.querydsl:querydsl-apt:jakarta:5.0.0
Gradle: com.querydsl:querydsl-codegen:5.0.0
Gradle: com.querydsl:querydsl-core:5.0.0
Gradle: com.querydsl:querydsl-jpa:jakarta:5.0.0
Gradle: com.sun.istack:istack-commons-runtime:4.1.2
Gradle: com.vaadin.external.google:android-json:0.0.20131108.vaadin1
Gradle: com.zaxxer:HikariCP:5.0.1
Gradle: io.github.classgraph:classgraph:4.8.149
Gradle: io.jsonwebtoken:jjwt-api:0.11.5
Gradle: io.jsonwebtoken:jjwt-impl:0.11.5
Gradle: io.jsonwebtoken:jjwt-jackson:0.11.5
Gradle: io.lettuce:lettuce-core:6.3.2.RELEASE
Gradle: io.micrometer:micrometer-commons:1.12.4
Gradle: io.micrometer:micrometer-core:1.12.4
Gradle: io.micrometer:micrometer-jakarta9:1.12.4
Gradle: io.micrometer:micrometer-observation:1.12.4
Gradle: io.netty:netty-buffer:4.1.107.Final
Gradle: io.netty:netty-codec:4.1.107.Final
Gradle: io.netty:netty-common:4.1.107.Final
Gradle: io.netty:netty-handler:4.1.107.Final
Gradle: io.netty:netty-resolver:4.1.107.Final
Gradle: io.netty:netty-transport-native-unix-common:4.1.107.Final
Gradle: io.netty:netty-transport:4.1.107.Final
Gradle: io.projectreactor:reactor-core:3.6.4
Gradle: io.smallrye:jandex:3.1.2
Gradle: io.swagger.core.v3:swagger-annotations-jakarta:2.2.7
Gradle: io.swagger.core.v3:swagger-core-jakarta:2.2.7
Gradle: io.swagger.core.v3:swagger-models-jakarta:2.2.7
Gradle: jakarta.activation:jakarta.activation-api:2.1.3
Gradle: jakarta.annotation:jakarta.annotation-api:2.1.1
Gradle: jakarta.inject:jakarta.inject-api:2.0.1
Gradle: jakarta.persistence:jakarta.persistence-api:3.1.0
Gradle: jakarta.transaction:jakarta.transaction-api:2.0.1
Gradle: jakarta.validation:jakarta.validation-api:3.0.2
Gradle: jakarta.xml.bind:jakarta.xml.bind-api:4.0.2
Gradle: javax.inject:javax.inject:1
Gradle: net.bytebuddy:byte-buddy-agent:1.14.12
Gradle: net.bytebuddy:byte-buddy:1.14.12
Gradle: net.java.dev.jna:jna-platform:5.13.0
Gradle: net.java.dev.jna:jna:5.13.0
Gradle: net.minidev:accessors-smart:2.5.0
Gradle: net.minidev:json-smart:2.5.0
Gradle: org.antlr:antlr4-runtime:4.13.0
Gradle: org.apache.commons:commons-lang3:3.9
Gradle: org.apache.logging.log4j:log4j-api:2.21.1
Gradle: org.apache.logging.log4j:log4j-to-slf4j:2.21.1
Gradle: org.apache.tomcat.embed:tomcat-embed-core:10.1.19
Gradle: org.apache.tomcat.embed:tomcat-embed-el:10.1.19
Gradle: org.apache.tomcat.embed:tomcat-embed-websocket:10.1.19
Gradle: org.apiguardian:apiguardian-api:1.1.2
Gradle: org.aspectj:aspectjweaver:1.9.21
Gradle: org.assertj:assertj-core:3.24.2
Gradle: org.awaitility:awaitility:4.2.0
Gradle: org.checkerframework:checker-qual:2.10.0
Gradle: org.checkerframework:checker-qual:3.37.0
Gradle: org.eclipse.angus:angus-activation:2.0.2
Gradle: org.eclipse.jdt:ecj:3.26.0
Gradle: org.glassfish.jaxb:jaxb-core:4.0.5
Gradle: org.glassfish.jaxb:jaxb-runtime:4.0.5
Gradle: org.glassfish.jaxb:txw2:4.0.5
Gradle: org.hamcrest:hamcrest:2.2
Gradle: org.hdrhistogram:HdrHistogram:2.1.12
Gradle: org.hibernate.common:hibernate-commons-annotations:6.0.6.Final
Gradle: org.hibernate.orm:hibernate-core:6.4.4.Final
Gradle: org.hibernate.validator:hibernate-validator:8.0.1.Final
Gradle: org.jboss.logging:jboss-logging:3.5.3.Final
Gradle: org.jsoup:jsoup:1.15.3
Gradle: org.junit.jupiter:junit-jupiter-api:5.10.2
Gradle: org.junit.jupiter:junit-jupiter-engine:5.10.2
Gradle: org.junit.jupiter:junit-jupiter-params:5.10.2
Gradle: org.junit.jupiter:junit-jupiter:5.10.2
Gradle: org.junit.platform:junit-platform-commons:1.10.2
Gradle: org.junit.platform:junit-platform-engine:1.10.2
Gradle: org.latencyutils:LatencyUtils:2.0.3
Gradle: org.mapstruct:mapstruct-processor:1.4.2.Final
Gradle: org.mapstruct:mapstruct:1.4.2.Final
Gradle: org.mariadb.jdbc:mariadb-java-client:3.3.3
Gradle: org.mockito:mockito-core:5.7.0
Gradle: org.mockito:mockito-junit-jupiter:5.7.0
Gradle: org.objenesis:objenesis:3.3
Gradle: org.opentest4j:opentest4j:1.3.0
Gradle: org.ow2.asm:asm:9.3
Gradle: org.projectlombok:lombok-mapstruct-binding:0.1.0
Gradle: org.projectlombok:lombok:1.18.30
Gradle: org.reactivestreams:reactive-streams:1.0.4
Gradle: org.skyscreamer:jsonassert:1.5.1
Gradle: org.slf4j:jcl-over-slf4j:2.0.12
Gradle: org.slf4j:jul-to-slf4j:2.0.12
Gradle: org.slf4j:slf4j-api:2.0.12
Gradle: org.springdoc:springdoc-openapi-starter-common:2.0.2
Gradle: org.springdoc:springdoc-openapi-starter-webmvc-api:2.0.2
Gradle: org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2
Gradle: org.springframework.boot:spring-boot-actuator-autoconfigure:3.2.4
Gradle: org.springframework.boot:spring-boot-actuator:3.2.4
Gradle: org.springframework.boot:spring-boot-autoconfigure:3.2.4
Gradle: org.springframework.boot:spring-boot-configuration-processor:3.2.4
Gradle: org.springframework.boot:spring-boot-devtools:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-actuator:3.2.1
Gradle: org.springframework.boot:spring-boot-starter-aop:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-data-jpa:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-data-redis:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-jdbc:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-json:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-logging:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-oauth2-client:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-security:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-test:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-tomcat:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-validation:3.2.4
Gradle: org.springframework.boot:spring-boot-starter-web:3.2.4
Gradle: org.springframework.boot:spring-boot-starter:3.2.4
Gradle: org.springframework.boot:spring-boot-test-autoconfigure:3.2.4
Gradle: org.springframework.boot:spring-boot-test:3.2.4
Gradle: org.springframework.boot:spring-boot:3.2.4
Gradle: org.springframework.data:spring-data-commons:3.2.4
Gradle: org.springframework.data:spring-data-jpa:3.2.4
Gradle: org.springframework.data:spring-data-keyvalue:3.2.4
Gradle: org.springframework.data:spring-data-redis:3.2.4
Gradle: org.springframework.security:spring-security-config:6.2.3
Gradle: org.springframework.security:spring-security-core:6.2.3
Gradle: org.springframework.security:spring-security-crypto:6.2.3
Gradle: org.springframework.security:spring-security-oauth2-client:6.2.3
Gradle: org.springframework.security:spring-security-oauth2-core:6.2.3
Gradle: org.springframework.security:spring-security-oauth2-jose:6.2.3
Gradle: org.springframework.security:spring-security-test:6.2.3
Gradle: org.springframework.security:spring-security-web:6.2.3
Gradle: org.springframework:spring-aop:6.1.5
Gradle: org.springframework:spring-aspects:6.1.5
Gradle: org.springframework:spring-beans:6.1.5
Gradle: org.springframework:spring-context-support:6.1.5
Gradle: org.springframework:spring-context:6.1.5
Gradle: org.springframework:spring-core:6.1.5
Gradle: org.springframework:spring-expression:6.1.5
Gradle: org.springframework:spring-jcl:6.1.5
Gradle: org.springframework:spring-jdbc:6.1.5
Gradle: org.springframework:spring-orm:6.1.5
Gradle: org.springframework:spring-oxm:6.1.5
Gradle: org.springframework:spring-test:6.1.5
Gradle: org.springframework:spring-tx:6.1.5
Gradle: org.springframework:spring-web:6.1.5
Gradle: org.springframework:spring-webmvc:6.1.5
Gradle: org.webjars:swagger-ui:4.15.5
Gradle: org.webjars:webjars-locator-core:0.55
Gradle: org.xmlunit:xmlunit-core:2.9.1
Gradle: org.yaml:snakeyaml:2.2
Gradle: p6spy:p6spy:3.9.1
```