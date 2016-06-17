FROM java:8-jre
WORKDIR /app
COPY build/libs/digest-job-tracker.jar /app/digest-job-tracker.jar
ENTRYPOINT ["java","-jar","/app/digest-job-tracker.jar"]
EXPOSE 8080
