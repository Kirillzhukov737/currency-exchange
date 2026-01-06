FROM tomcat:10.1-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/currency_exchange_war_exploded.war /usr/local/tomcat/webapps/currency_exchange_war_exploded.war

EXPOSE 8080
