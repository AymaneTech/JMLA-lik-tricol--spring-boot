FROM tomcat:10.1-jdk17
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/tricol_db
ENV SPRING_DATASOURCE_USERNAME=tricol_user
ENV SPRING_DATASOURCE_PASSWORD=tricol_pass123
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]

