FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} gaji.jar
ENTRYPOINT ["java","-jar","/gaji.jar"]