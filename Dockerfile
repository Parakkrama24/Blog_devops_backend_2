FROM openjdk:21
ARG JAR_FILE=target/*.jar

# Set environment variables (optional, can be set at runtime too)
ENV MYSQL_HOST=jenkins-mysql-db.cc3c8i2skz7p.us-east-1.rds.amazonaws.com
ENV MYSQL_PORT=3306
ENV MYSQL_DB_NAME=blog
ENV MYSQL_USER=admin
ENV MYSQL_PASSWORD=StrongPassword123!

COPY ${JAR_FILE} app.jar



EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]