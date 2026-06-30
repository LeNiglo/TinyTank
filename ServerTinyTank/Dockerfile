# --- build stage: Maven + JDK 21 produce the fat JAR ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

# --- runtime stage: JRE 21 + the game data read at startup ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/target/ServerTinyTank-1.8-jar-with-dependencies.jar ./ServerTinyTank.jar
# Config + map data are read by relative path at runtime (not bundled in the JAR)
COPY config.properties tanks.json obstacles.json ./
COPY maps ./maps

# KryoNet TCP + UDP game ports
EXPOSE 13333/tcp
EXPOSE 13444/udp

# Console mode (headless — the Swing GUI can't run in a container).
# The server binds its ports only once a game is started from the console, so run
# the container interactively (compose: stdin_open + tty) and issue `start`.
CMD ["java", "-jar", "ServerTinyTank.jar", "-c"]
