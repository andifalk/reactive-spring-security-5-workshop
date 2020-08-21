@echo off

setlocal

rem replace this config with your own directory
set WORKSHOP_HOME=C:\reactive-spring-security-5-workshop\setup

docker run --rm -p 8080:8080 -e DB_VENDOR=h2 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -e KEYCLOAK_IMPORT=/tmp/keycloak_realm_workshop.json -v %WORKSHOP_HOME%\keycloak_realm_workshop.json:/tmp/keycloak_realm_workshop.json jboss/keycloak:11.0.1
