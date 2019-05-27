# Introduction Labs

The intro labs contain the following demos:

* [Reactive Programming Playground](reactive-playground)
* [OAuth2 Authorization Code Grant Flow Demo](auth-code-demo)
* [Connecting a client app to GitHub API using OAuth 2.0](github-client)

The first intro lab includes several junit test cases showing how to 
start with reactive programming using project reactor and the publisher types
_Mono_ and _Flux_.

The second one shows all steps of the authorization code grant flow
in detail to understand how this flow works. This is important for other concepts
that are built upon this flow like the PKCE addition or OpenID Connect 1.0.

The third one shows how the common predefined OAuth2 clients
of spring security 5 can be used to quickly build clients
for well known providers like GitHub, Google or Facebook. 
