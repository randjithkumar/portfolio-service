# =========================
# Stage 1: Build application
# =========================
FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .

RUN mvn -B -ntp dependency:go-offline

COPY src ./src

RUN mvn -B -ntp clean package -DskipTests


# ==================================
# Stage 2: Extract Spring Boot layers
# ==================================
FROM eclipse-temurin:21-jre AS builder

WORKDIR /builder

COPY --from=build \
    /workspace/target/portfolio-service-0.0.1-SNAPSHOT.jar \
    application.jar

RUN java -Djarmode=tools \
    -jar application.jar \
    extract \
    --layers \
    --destination extracted


# ==========================
# Stage 3: Runtime container
# ==========================
FROM eclipse-temurin:21-jre

WORKDIR /application

RUN groupadd --system portfolio \
    && useradd \
        --system \
        --gid portfolio \
        --home-dir /application \
        portfolio

COPY --from=builder \
    /builder/extracted/dependencies/ ./

COPY --from=builder \
    /builder/extracted/spring-boot-loader/ ./

COPY --from=builder \
    /builder/extracted/snapshot-dependencies/ ./

COPY --from=builder \
    /builder/extracted/application/ ./

RUN chown -R portfolio:portfolio /application

USER portfolio

EXPOSE 8081

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar application.jar"]