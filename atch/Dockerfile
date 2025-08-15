FROM maven:3.9.6-amazoncorretto-21 AS deps
WORKDIR /app
COPY pom.xml ./
COPY mvnw ./
COPY mvnw.cmd ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

FROM maven:3.9.6-amazoncorretto-21
WORKDIR /app
ENV JAVA_OPTS="-Xmx512m"

# Instalar curl para health checks
RUN yum update -y && yum install -y curl && yum clean all

COPY --from=deps /root/.m2 /root/.m2
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
COPY src ./src
RUN chmod +x mvnw && ./mvnw clean package -DskipTests -B
EXPOSE 8080
CMD ["java", "-jar", "target/atch-0.0.1-SNAPSHOT.jar"]
