FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD uavmanagesystem-0.0.2.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]