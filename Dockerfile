#FROM bellsoft/liberica-openjdk-alpine:latest-aarch64

FROM bellsoft/liberica-openjdk-alpine:latest
MAINTAINER Bernd Heidemann <b.heidemannk@schule.bremen.de>

ARG JAR_FILE
#COPY target/${JAR_FILE} /employee.jar
COPY target/employeemanagement_authentik-1.0.0.jar /employee.jar

ARG SPRING_BOOT_PROFILE

ENV WEB_IP "$WEB_ADDRESS_INT"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/employee.jar"]