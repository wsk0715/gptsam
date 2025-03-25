# Base Image
FROM openjdk:17-jdk-slim

# Working Directory
WORKDIR /app

# 빌드된 jar 파일을 컨테이너 내 /app 디렉토리로 복사
COPY ./build/libs/*.jar app.jar

# 컨테이너가 시작될 때 실행할 명령어 지정
ENTRYPOINT ["java", "-jar", "app.jar"]
