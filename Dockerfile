FROM openjdk:12
LABEL seongwon="seongwon@edu.hanbat.ac.kr"

COPY ./build/libs/login-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD [ "java", "-jar", "login-0.0.1-SNAPSHOT.jar"]