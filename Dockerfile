FROM openjdk:14-jre-hotspot AS build
COPY ./ /app
WORKDIR /app
RUN mvn clean package -U

FROM openjdk:8u312-oraclelinux8
RUN mkdir /app
WORKDIR /app
COPY --from=build ./app/api/target/api-1.0-SNAPSHOT.jar /app
EXPOSE 8081

CMD ["java", "-jar", "api-1.0-SNAPSHOT.jar"]