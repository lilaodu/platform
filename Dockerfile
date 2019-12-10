FROM java:8
VOLUME /tmp
ADD platform-0.0.1-SNAPSHOT.jar platform.jar
EXPOSE 8880
ENTRYPOINT ["java","-jar","platform.jar"]
