# Spring Security 5.1 Workshop

## Authentication in Servlet Web Stack

This contains a demo project that has configured basic and form based authentication
for the servlet web stack (blocking).

### Configuration

The spring security configuration for the servlet web stack can be found
in class _WebSecurityConfiguration_.

### Running the application

Just execute class _AuthenticationServletDemoApplication_ to start the demo application.

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

1. ExceptionTranslationFilter.handleSpringSecurityException()
2. DelegatingAuthenticationEntryPoint.commence()
3. BasicAuthenticationEntryPoint.commence()
4. BasicAuthenticationFilter.doFilterInternal()	
5. ProviderManager.authenticate()
6. AbstractUserDetailsAuthenticationProvider.authenticate()
7. UserDetailsService.loadUserByUsername()
8. InMemoryUserDetailsManager.loadUserByUsername()
9. DaoAuthenticationProvider.additionalAuthenticationChecks()

### Form login authentication flow

The authentication mechanism for __form login authentication__ can be followed by setting 
debugging breakpoints in the following locations and restart the application
in debug mode:

1. ExceptionTranslationFilter.handleSpringSecurityException()
2. DelegatingAuthenticationEntryPoint.commence()
3. LoginUrlAuthenticationEntryPoint.commence()
4. DefaultLoginPageGeneratingFilter.generateLoginPageHtml()
5. UsernamePasswordAuthenticationFilter.attemptAuthentication()
6. ProviderManager.authenticate()
7. AbstractUserDetailsAuthenticationProvider.authenticate()
8. DaoAuthenticationProvider.retrieveUser()
9. UserDetailsService.loadUserByUsername()
10. InMemoryUserDetailsManager.loadUserByUsername()
11. DaoAuthenticationProvider.additionalAuthenticationChecks()
12. UsernamePasswordAuthenticationFilter.successfulAuthentication()
13. SavedRequestAwareAuthenticationSuccessHandler.onAuthenticationSuccess()
