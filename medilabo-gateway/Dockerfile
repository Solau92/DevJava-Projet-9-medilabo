FROM eclipse-temurin:17-jdk-jammy

ARG src="target/medilabo-gateway-*.jar"
ARG dest="medilabo-gateway.jar"

COPY ${src} ${dest}

ENTRYPOINT ["java","-jar","medilabo-gateway.jar"]