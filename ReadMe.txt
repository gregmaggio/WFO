Using Swagger to generate documentation for the service

1) Need to clone swagger and build it locally
git clone https://github.com/swagger-api/swagger-codegen
cd swagger-codegen
mvn clean package

2) Need to deploy the web service so that the swagger.json is running locally via tomcat

3) Need to generate the documentation

java -jar modules/swagger-codegen-cli/target/swagger-codegen-cli.jar generate -i http://localhost:8081/WFO/api-docs -l dynamic-html -o C:/Dev/Applications/WFO/src/main/webapp

4) Redeploy the web service with the new documentation

Deploying to the cloud:

Make sure that you change the log file location to ${catalina.base}/logs/wfo.txt
