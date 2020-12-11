FROM maven:3-jdk-11

WORKDIR /usr/src/app

ENV PORT=8080

EXPOSE 8080

COPY . ./

RUN mvn -DskipTests clean dependency:list install

CMD [ "sh", "target/bin/webapp" ]