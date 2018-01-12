@REM Or "mvn spring-boot:run"
java -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=n -jar %~dp0\puzzle-service-console\target\puzzle-service-console-0.0.1-SNAPSHOT.jar -q
