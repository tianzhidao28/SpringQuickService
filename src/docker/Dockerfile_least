FROM dockerfile/java:oracle-java7
ADD service-demo.jar /opt/projectName/
EXPOSE 8080
WORKDIR /opt/projectName/
CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "service-demo.jar"]
