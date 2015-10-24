
FROM ubuntu:latest

MAINTAINER rocyuan roc

RUN apt-get update

RUN apt-get install default-jre -y

RUN apt-get install default-jdk -y

RUN apt-get install maven -y

ADD pom.xml /app/

ADD src/ /app/src/

WORKDIR /app/

# RUN mvn spring-boot run
RUN mvn clean package
EXPOSE  8080

# CMD mvn spring-boot:run -Ddetail=true -Dgoal=compile
# CMD mvn spring-boot run
# CMD ["java","-jar","target/service-demo.jar"]
