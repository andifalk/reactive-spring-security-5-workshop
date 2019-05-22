# Intro Labs: Client app to GitHub API

This introduction lab shows how easy it is to use one of the well known OAuth2/OIDC provider
like GitHub, Google or Facebook with Spring Security.

Spring Security provides the class _CommonOAuth2Provider_ containing predefined configurations
for setting up one of these providers in your client application:

* Google
* GitHub
* Facebook
* Okta 

For details please see the corresponding section in the
[spring security reference doc](https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#oauth2login-common-oauth2-provider)

This lab implements a simple client to display notifications for the GitHub user who will
authorize this client to use his/her GitHub credentials.

The relevant OAuth2 configuration part is quite simple and is located in
_application.yml_ file:

```
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: <clientid>
            client-secret: <client_secret>
            scope:
              - read:user
              - notifications
            redirect-uri: '{baseUrl}/login/oauth2/code/{registrationId}'
```

As you can see there are placeholders or _clientid_ and _client_secret_.
To get these credentials you need a GitHub account, after logging into our account:

1. Go to your personal settings 
2. Then select _Developer Settings_, select _OAuth Apps_ and click on _New OAuth App_
3. Use _Notification-Client_ as application name
4. Use _http://localhost:9090/login/oauth2/code/github_ as redirect uri
5. Use _http://localhost:9090_ as homepage url
6. Click on _Register application'
7. Now you should see the generated values for _Client ID_ and _Client Secret_
8. Copy these values over the placeholders in _application.yml_ file

No start the main class _com.example.github.GitHubClientApplication_ and 
browse to [localhost:9090](http://localhost:9090). 

After you login into GitHub you should see
user attributes and you should be able to get the notifications by clicking on the button on the top of the screen.
 







