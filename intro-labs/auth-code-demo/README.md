# Intro-Lab: OAuth 2.0 Authorization Code Grant Flow in Details

A client demonstrating all steps of an OAuth 2.0 Authorization Code Grant Flow.

__Important note:   
This demo client is just for demonstrating purposes. All the mandatory validations
and recommended security precautions of a production-ready OAuth 2.0 client library are missing here. So do NOT use any code
of this client for production !!!!__

## Authorization code grant flow in detail

The authorization code grant flow is the base flow mostly used in today's applications.
It is used for resource owner authorization in lots of internet services like [slideshare](https://www.slideshare.net/) 
or [stackoverflow](https://stackoverflow.com/). 

In enterprise applications it is used for authentication in
conjunction with OpenID Connect. Even for single page applications this flow (with the addition of PKCE) 
is now recommended as well.
 
To see all details for this grant flow see the corresponding section of the 
[OAuth 2.0 Authorization Framework Spec](https://tools.ietf.org/html/rfc6749#section-4.1).

1. The flow starts with the authorization request, this redirects to the authorization server.
   Here the user logs in using his credentials and approves a consent page
2. After successful login a 302 HTTP redirect request with the authorization code is sent through to the browser which redirects
   to the callback entry point provided by the client application 
3. Now the client application send a token request to the authorization server to exchange
   the authorization code into an access token
   
You can see each of these steps in the demo client application of this intro lab.
Usually only step 1 is visible to a user of the client. Steps 2 and 3 are only visible here
to visualize the whole flow.

In addition the demo client can also call the token introspection endpoint to verify if a 
token is still valid and get a new access token by using the refresh token.
           
## Run the demo application           
                
To start the demo:

* Make sure _Keycloak_ is running correctly
* Browse to [localhost:9095/client](http://localhost:9095/client) to start the demo client                  

__Important:__ You can use one of the following users to login:

| Username | Email                    | Password | Role            |
| ---------| ------------------------ | -------- | --------------- |
| bwayne   | bruce.wayne@example.com  | wayne    | LIBRARY_USER    |
| bbanner  | bruce.banner@example.com | banner   | LIBRARY_USER    |
| ckent    | clark.kent@example.com   | kent     | LIBRARY_ADMIN   |

You may use the _username_ or _email_ in the username input field.

If you stay too long on step 2 (where you have retrieved the authorization code) then you might
get an error when proceeding to step 3 (exchanging the code for an access token).  
This is because the authorization code timed out.

According to the [OAuth2 specification](https://tools.ietf.org/html/rfc6749#section-4.1.2):

<blockquote cite="https://tools.ietf.org/html/rfc6749#section-4.1.2">
The authorization code MUST expire shortly after it is issued to mitigate the risk of leaks.  
A maximum authorization code lifetime of 10 minutes is RECOMMENDED. 
The client MUST NOT use the authorization code more than once. 
</blockquote>





