FROM maven:3-eclipse-temurin-17 as build
WORKDIR /workspace/cg
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
COPY --from=build /workspace/cg/cityguard-server/target/cityguard-server.jar /workspace/cg/server.jar
EXPOSE 8088/tcp
WORKDIR /workspace/cg
COPY test-configuration/ /config/
ENTRYPOINT java $JAVA_OPTS -jar server.jar --spring.config.location=file:/config/application.yml