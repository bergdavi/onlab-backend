FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
ARG PUBLIC_HTML=target/public_html
COPY ${JAR_FILE} app.jar
COPY ${PUBLIC_HTML} /public/
ENTRYPOINT ["java","-jar","/app.jar"] 
