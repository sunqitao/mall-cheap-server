FROM openjdk:8
ENV PROJECT mall-cheap-api

COPY target/${PROJECT}-*-*.jar /opt/$PROJECT/${PROJECT}.jar
WORKDIR /opt/$PROJECT
EXPOSE 9090
ENTRYPOINT java -Duser.timezone=Asia/Shanghai -jar /opt/$PROJECT/${PROJECT}.jar