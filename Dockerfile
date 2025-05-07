# OpenJDK 17 이미지 사용
FROM openjdk:17-jdk-slim

# 인자 설정 - Jar_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복사
COPY ${JAR_FILE} app.jar

# MySQL 설치
FROM mysql:8.0

# MySQL 환경 변수 설정 (root 비밀번호를 12345로 설정)
ENV MYSQL_ROOT_PASSWORD=12345

# MySQL 포트 열기
EXPOSE 3306

# Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/app.jar"]

