# Spring Security 5.1 Workshop

## Authentication in Reactive Web Stack

This contains a demo project that has configured basic and form based authentication
for the reactive web stack (webflux).

### Configuration

The spring security configuration for the webflux web stack can be found
in class _ReactiveWebSecurityConfiguration_.

### Running the application

Just execute class _AuthenticationReactiveDemoApplication_ to start the demo application.

Then you can login into the application either using

* Basic authentication (via command line rest client)
* Form based login (via web browser)

The login credentials are username=_user_ and password=_secret_.

If you are using a command line rest client (e.g. Httpie) the use the following command:

```
http localhost:8080 --auth user:secret
```

In case you want to use the web browser then just navigate to the url http://localhost:8080.
Here you get a login form to specify the credentials.

In either way you should finally see the string __"it works!"__.

### Basic authentication flow

The authentication mechanism for __basic authentication__ can be followed by setting 
debugging breakpoints in the following locations and restart the application
in debug mode:

1. ExceptionTranslationWebFilter.commenceAuthentication()
2. HttpBasicServerAuthenticationEntryPoint.commence()
3. AuthenticationWebFilter.authenticate()
4. UserDetailsRepositoryReactiveAuthenticationManager.authenticate()
5. MapReactiveUserDetailsService.findByUsername()
6. AuthenticationWebFilter.onAuthenticationSuccess()
7. WebFilterChainServerAuthenticationSuccessHandler.onAuthenticationSuccess()

### Form login authentication flow

The authentication mechanism for __form login authentication__ can be followed by setting 
debugging breakpoints in the following locations and restart the application
in debug mode:

1. ExceptionTranslationWebFilter.commenceAuthentication()
2. RedirectServerAuthenticationEntryPoint.commence()
3. LoginPageGeneratingWebFilter.render()
4. AuthenticationWebFilter.authenticate()
5. UserDetailsRepositoryReactiveAuthenticationManager.authenticate()
6. MapReactiveUserDetailsService.findByUsername()
7. AuthenticationWebFilter.onAuthenticationSuccess()
8. WebFilterChainServerAuthenticationSuccessHandler.onAuthenticationSuccess()