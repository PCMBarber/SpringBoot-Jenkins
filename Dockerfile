FROM openjdk:11-jre-slim
COPY ./wars/project_war.war /usr/app/project_war.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/project_war.war"]
