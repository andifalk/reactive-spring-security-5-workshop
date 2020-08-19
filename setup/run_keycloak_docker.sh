#!/bin/sh

# replace this config with your own directory
export WORKSHOP_HOME=/home/afa/development/localgit/secure-oauth2-oidc-workshop/setup

docker run --rm -p 8080:8080 -e DB_VENDOR=h2 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin \
    -e KEYCLOAK_IMPORT=/tmp/keycloak_realm_workshop.json \
    -v $WORKSHOP_HOME/keycloak_realm_workshop.json:/tmp/keycloak_realm_workshop.json jboss/keycloak:10.0.2
