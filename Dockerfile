# OpenJDK 17 이미지 사용
FROM openjdk:17-jdk-slim

# 인자 설정 - Jar_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복사
COPY ${JAR_FILE} app.jar

# Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "/app.jar"]

