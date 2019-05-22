#!/bin/sh

export KEYCLOAK_HOME=/home/afa/development/keycloak-6.0.1
export PATH=$PATH:$KEYCLOAK_HOME/bin

#-- login
kcadm.sh config credentials --server http://localhost:8080/auth --realm master --user admin --password admin

#-- create realm 'workshop'
kcadm.sh create realms -s realm=workshop -s enabled=true

# create roles
kcadm.sh create roles -r workshop -s name=library_user -s 'description=Regular library user'
kcadm.sh create roles -r workshop -s name=library_curator -s 'description=Library curator'
kcadm.sh create roles -r workshop -s name=library_admin -s 'description=Library administrator'

# create groups

kcadm.sh create groups -r workshop -s name=library_user
kcadm.sh create groups -r workshop -s name=library_curator
kcadm.sh create groups -r workshop -s name=library_admin

# create users

kcadm.sh create users -r workshop -s username=bwayne -s firstName=Bruce -s lastName=Wayne -s 'email=bruce.wayne@example.com' -s emailVerified=true -s enabled=true
kcadm.sh create users -r workshop -s username=bbanner -s firstName=Bruce -s lastName=Banner -s 'email=bruce.banner@example.com' -s emailVerified=true -s enabled=true
kcadm.sh create users -r workshop -s username=pparker -s firstName=Peter -s lastName=Parker -s 'email=peter.parker@example.com' -s emailVerified=true -s enabled=true
kcadm.sh create users -r workshop -s username=ckent -s firstName=Clark -s lastName=Kent -s 'email=clark.kent@example.com' -s emailVerified=true -s enabled=true

# set passwords for users

kcadm.sh set-password -r workshop --username bwayne --new-password wayne
kcadm.sh set-password -r workshop --username bbanner --new-password banner
kcadm.sh set-password -r workshop --username pparker --new-password parker
kcadm.sh set-password -r workshop --username ckent --new-password kent

# set roles to users

kcadm.sh add-roles --uusername bwayne --rolename library_user -r workshop
kcadm.sh add-roles --uusername bbanner --rolename library_user -r workshop
kcadm.sh add-roles --uusername pparker --rolename library_curator -r workshop
kcadm.sh add-roles --uusername ckent --rolename library_admin -r workshop

# assign groups to users

bwayne_userid=$(kcadm.sh get users -r workshop -q username=bwayne --fields id | jq '.[0].id' -r)
bbanner_userid=$(kcadm.sh get users -r workshop -q username=bbanner --fields id | jq '.[0].id' -r)
pparker_userid=$(kcadm.sh get users -r workshop -q username=pparker --fields id | jq '.[0].id' -r)
ckent_userid=$(kcadm.sh get users -r workshop -q username=ckent --fields id | jq '.[0].id' -r)

user_groupid=$(kcadm.sh get groups -r workshop | jq '.[] | select(.name == "library_user")' | jq '.id' -r)
curator_groupid=$(kcadm.sh get groups -r workshop | jq '.[] | select(.name == "library_curator")' | jq '.id' -r)
admin_groupid=$(kcadm.sh get groups -r workshop | jq '.[] | select(.name == "library_admin")' | jq '.id' -r)

kcadm.sh update users/$bwayne_userid/groups/$user_groupid -r workshop -s realm=workshop -s userId=$bwayne_userid -s groupId=$user_groupid -n
kcadm.sh update users/$bbanner_userid/groups/$user_groupid -r workshop -s realm=workshop -s userId=$bbanner_userid -s groupId=$user_groupid -n
kcadm.sh update users/$pparker_userid/groups/$curator_groupid -r workshop -s realm=workshop -s userId=$pparker_userid -s groupId=$curator_groupid -n
kcadm.sh update users/$ckent_userid/groups/$admin_groupid -r workshop -s realm=workshop -s userId=$ckent_userid -s groupId=$admin_groupid -n

# create clients

kcadm.sh create clients -r workshop -s 'name=Demo Client' -s clientId=demo-client -s enabled=true -s standardFlowEnabled=true \
 -s implicitFlowEnabled=false -s directAccessGrantsEnabled=true -s serviceAccountsEnabled=false -s publicClient=false -s clientAuthenticatorType=client-secret \
 -s secret=b3ec9d3f-d1ee-4a18-b4ba-05d832c15293 -s protocol=openid-connect -s fullScopeAllowed=true \
 -s 'redirectUris=["http://localhost:9095/client/callback"]' -s 'webOrigins=["http://localhost:9095"]'

kcadm.sh create clients -r workshop -s 'name=Library Client' -s clientId=library-client -s enabled=true -s standardFlowEnabled=true \
 -s implicitFlowEnabled=false -s directAccessGrantsEnabled=true -s serviceAccountsEnabled=true -s publicClient=false -s clientAuthenticatorType=client-secret \
 -s secret=9584640c-3804-4dcd-997b-93593cfb9ea7 -s protocol=openid-connect -s fullScopeAllowed=true \
 -s 'redirectUris=["http://localhost:9090/library-client/login/oauth2/code/keycloak"]' -s 'webOrigins=["http://localhost:9090"]'

# add groups mapper

library_clientid=$(kcadm.sh get clients -r workshop -q clientId=library-client --fields id | jq '.[0].id' -r)

kcadm.sh create clients/$library_clientid/protocol-mappers/models -r workshop -f - << EOF
{   "name" : "Library-Groups",
    "protocol" : "openid-connect",
    "protocolMapper" : "oidc-group-membership-mapper",
    "consentRequired" : false,
    "config" : {
      "full.path" : "false",
      "id.token.claim" : "true",
      "access.token.claim" : "true",
      "claim.name" : "groups",
      "userinfo.token.claim" : "true"
    }
}
EOF

kcadm.sh create clients/$library_clientid/protocol-mappers/models -r workshop -f - << EOF
{
    "name" : "Library-Service",
    "protocol" : "openid-connect",
    "protocolMapper" : "oidc-audience-mapper",
    "consentRequired" : false,
    "config" : {
      "id.token.claim" : "false",
      "access.token.claim" : "true",
      "included.custom.audience" : "library-service"
    }
}
EOF