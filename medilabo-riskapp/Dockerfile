FROM eclipse-temurin:17-jdk-jammy

ARG src="target/medilabo-riskapp-*.jar"
ARG dest="medilabo-riskapp.jar"

COPY ${src} ${dest}

ENTRYPOINT ["java","-jar","medilabo-riskapp.jar"]