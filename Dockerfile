FROM openjdk:17-jdk-slim

WORKDIR /usrapp/bin

ENV PORT 8080

COPY /target/classes /usrapp/bin/classes
COPY /target/dependency /usrapp/bin/dependency

ENV AWS_ACCESS_KEY_ID="AWS_ACCESS_KEY_ID_FAKE"
ENV AWS_SECRET_ACCESS_KEY="AWS_SECRET_ACCESS_KEY_FAKE"
ENV AWS_SESSION_TOKEN="AWS_SESSION_TOKEN_FAKE"

CMD ["java","-cp","./classes:./dependency/*","com.example.Application"]