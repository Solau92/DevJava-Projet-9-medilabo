FROM eclipse-temurin:17-jdk-jammy

ARG src="target/medilabo-noteapp-*.jar"
ARG dest="medilabo-noteapp.jar"

COPY ${src} ${dest}

ENTRYPOINT ["java","-jar","medilabo-noteapp.jar"]