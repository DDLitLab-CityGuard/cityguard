FROM maven:3-eclipse-temurin-17 as build
WORKDIR /workspace/cg
COPY . .
RUN mvn install
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
ENV SPRING_CONFIG_LOCATION=classpath:/application.yml
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
COPY --from=build /workspace/cg/cityguard-server/target/cityguard-server.jar /workspace/cg/server.jar
EXPOSE 8088/tcp
WORKDIR /workspace/cg
ENTRYPOINT java $JAVA_OPTS -jar server.jar --spring.config.location=$SPRING_CONFIG_LOCATION