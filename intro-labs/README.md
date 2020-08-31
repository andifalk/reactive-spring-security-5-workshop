# Introduction Labs

The intro labs contain the following demos:

* [Reactive Programming Playground](reactive-playground)
* [Migrate to Reactive Demo](migrate-to-reactive)
* [Reactive Authentication Demo](authentication-reactive-demo)
* [Servlet Authentication Demo](authentication-servlet-demo)
* [OAuth2 Authorization Code Grant Flow Demo](auth-code-demo)
* [Connecting a client app to GitHub API using OAuth 2.0](github-client)

The __first__ intro lab includes several junit test cases showing how to 
start with reactive programming using project reactor and the publisher types
_Mono_ and _Flux_.

The _second_ intro lab shows how to migrate existing client and server applications to the reactive Spring WebFlux world.

The __third__ and __fourth__ one show the differences in the basic configuration between
spring security configuration of authentication for reactive and non-reactive web stacks. 

The __fifth__ one shows all steps of the authorization code grant flow
in detail to understand how this flow works. This is important for other concepts
that are built upon this flow like the PKCE addition or OpenID Connect 1.0.

The __last__ one shows how the common predefined OAuth2 clients
of spring security 5 can be used to quickly build clients
for well known providers like GitHub, Google or Facebook. 
