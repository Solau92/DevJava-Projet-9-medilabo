FROM eclipse-temurin:17-jdk-jammy

ARG src="target/medilabo-patientapp-*.jar"
ARG dest="medilabo-patientapp.jar"

COPY ${src} ${dest}

ENTRYPOINT ["java","-jar","medilabo-patientapp.jar"]