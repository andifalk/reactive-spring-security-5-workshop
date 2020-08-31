# Migration Path To Reactive

These applications show how to migrate a blocking servlet based client/server application to the corresponding
reactive variants.

You find the following applications:

* [initial-server-application](initial-server-application): This is the initial servlet based Spring MVC server application to migrate to the reactive world.
* [reactive-server-application](reactive-server-application): This is the migrated application using the reactive Spring WebFlux stack.
* [initial-client-application](initial-client-application): This is the initial client application using a blocking _RestTemplate_ to access the server
* [web-client-application](web-client-application): This is the migrated client application using the reactive _WebClient_ to access the server
 