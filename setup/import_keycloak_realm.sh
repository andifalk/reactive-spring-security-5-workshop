#!/bin/sh

# replace this with your own installation directory of keycloak
export KEYCLOAK_HOME=/home/afa/development/keycloak-10.0.2

$KEYCLOAK_HOME/bin/standalone.sh -Dkeycloak.migration.action=import \
-Dkeycloak.migration.realmName=workshop \
-Dkeycloak.migration.provider=singleFile -Dkeycloak.migration.file=./keycloak_realm_workshop.json \
-Dkeycloak.migration.strategy=OVERWRITE_EXISTING
