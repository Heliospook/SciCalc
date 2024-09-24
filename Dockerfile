FROM amazoncorretto:21
WORKDIR /app
COPY target/SciCalc-1.0.jar /app/SciCalc.jar
EXPOSE 3000
ENTRYPOINT ["java", "-jar", "SciCalc.jar"]

