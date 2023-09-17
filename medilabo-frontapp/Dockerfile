FROM eclipse-temurin:17-jdk-jammy

ARG src="target/medilabo-frontapp-*.jar"
ARG dest="medilabo-frontapp.jar"

COPY ${src} ${dest}

ENTRYPOINT ["java","-jar","medilabo-frontapp.jar"]