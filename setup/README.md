# Requirements and Setup

## Requirements

* [Java SDK](https://adoptopenjdk.net) Version 8, 11 or 14
* A Java IDE like
  * [Eclipse](https://www.eclipse.org/downloads)
  * [Spring Toolsuite](https://spring.io/tools)
  * [IntelliJ](https://www.jetbrains.com/idea/download)
  * [Visual Studio Code](https://code.visualstudio.com)
* [Keycloak](https://keycloak.org)
* [Docker](https://docs.docker.com/engine/install) if you want to run Keycloak as docker container
* [Git](https://git-scm.com)
* [MongoDB Compass](https://www.mongodb.com/try/download/compass) or [Robo 3T](https://robomongo.org) to look inside the embedded MongoDB instance
* [Postman](https://www.getpostman.com/downloads), [Httpie](https://httpie.org/#installation), or [Curl](https://curl.haxx.se/download.html) for REST calls

In case you select [Postman](https://www.getpostman.com/downloads), then the provided [Postman Collection](oidc_workshop.postman_collection.json) might be helpful.
Just import this [Postman Collection (Version 2.1 format)](oidc_workshop.postman_collection.json) into Postman.

### IntelliJ

IntelliJ does not require any specific additional plugins or configuration.

### Eclipse IDE

If you are an Eclipse user, then the usage of the Eclipse-based [Spring ToolSuite](https://spring.io/tools) is strongly recommended.
This eclipse variant already has all the required gradle and spring boot support pre-installed.

In case you want to stick to your plain Eclipse installation then you have to add the following features via the
eclipse marketplace: 

* BuildShip Gradle Integration (Version 3.x). This might be already pre-installed depending 
on your eclipse variant (e.g. Eclipse JavaEE) installed.
* Spring Tools 4 for Spring Boot (Spring Tool Suite 4).

### Visual Studio Code

To be able to work properly in Visual Studio Code with this Spring Boot Java Gradle project you need at least these extensions:

* Java Extension Pack
* vscode-gradle-language
* VS Code Spring Boot Application Development Extension Pack

## Get the source code
                       
Clone this GitHub repository (https://github.com/andifalk/reactive-spring-security-5-workshop):

```
git clone https://github.com/andifalk/reactive-spring-security-5-workshop.git reactive_workshop
```

After that you can import the whole workshop project directory into your IDE as a __gradle project__:

* [IntelliJ](https://www.jetbrains.com/idea): Open menu item "New project from existing sources..." and then select 'Gradle' when prompted
* [Eclipse](https://www.eclipse.org/) or [Spring ToolSuite](https://spring.io/tools): Open menu item "Import/Gradle/Existing gradle project"
* [Visual Studio Code](https://code.visualstudio.com/): Just open the root directory in VS Code and wait until VS Code has configured the project

## Run the java applications

All spring boot based java projects can either be run using your Java IDE or using the command line
with changing into the corresponding project directory and issuing a `gradlew bootRun` command.

In this workshop we will use [Keycloak](https://keycloak.org) by JBoss/RedHat as local identity provider.  
[Keycloak](https://keycloak.org) is [certified for OpenID Connect 1.0](https://openid.net/developers/certified/) and 
implements OAuth 2.0 and OpenID Connect 1.0.

## Setup Keycloak

You need a compliant OAuth 2.0 / OpenID Connect provider for this workshop.
Here we will use [Keycloak](https://keycloak.org) by RedHat/JBoss.

To set up Keycloak you have 2 options:

1. Run Keycloak using Docker (if you have Docker installed)
2. Local Keycloak installation & configuration

### Using Docker

If you have Docker installed then setting up Keycloak is quite easy.

To configure and run Keycloak using docker

1. Open a new command line terminal window
2. Change directory to subdirectory _setup_keycloak_ of the workshop repository
3. Open and edit the script _run_keycloak_docker.sh_ or _run_keycloak_docker.bat_ (depending on your OS) and adapt the value for _WORKSHOP_HOME_ to your local workshop repository directory
4. Save and execute the script _run_keycloak_docker.sh_ or _run_keycloak_docker.bat_ (depending on your OS)

Wait until the docker container has been started completely. When you see the line _Started 590 of 885 services_, 
then Keycloak is configured and running.  
Now open your web browser and navigate to [localhost:8080/auth/admin](http://localhost:8080/auth/admin) and login
using the credentials _admin_/_admin_.

### Local Installation

To set up [Keycloak](https://keycloak.org): 

1. Download the [Standard Server Distribution of Keycloak (Version 10.0.x)](https://www.keycloak.org/downloads-archive.html).
2. Extract the downloaded zip/tar file __keycloak-x.x.x.zip__/__keycloak-x.x.x.tar-gz__ into a new local directory of your choice 
(this directory will be referenced as __<KEYCLOAK_INSTALL_DIR>__ in next steps)

This workshop requires a pre-defined configuration for Keycloak (i.e. some OAuth2/OpenID Connect clients, and user accounts).

To configure Keycloak you need to have checked out the GIT repository for this workshop.
All you need to configure Keycloak is located in the subdirectory _setup_keycloak_ of the repository.

1. Change into the subdirectory _setup_keycloak_ of the workshop git repository
2. Open the file __import_keycloak_realm.sh__ or __import_keycloak_realm.bat__ (depending on your OS) in the _setup_keycloak_ subdirectory 
   and change the value of the environment variable _KEYCLOAK_HOME_ to your __<KEYCLOAK_INSTALL_DIR>__ of step 2 and save the file
3. Now open a new command-line terminal window, change into the subdirectory _setup_keycloak_ again and execute the provided script
   __import_keycloak_realm.sh__ or __import_keycloak_realm.bat__ (depending on your OS). 
   This starts a standalone Keycloak instance and automatically imports the required configuration.
4. Wait until the import has finished (look for a line like _Started 590 of 885 services_) then 
   direct your web browser to [localhost:8080/auth](http://localhost:8080/auth/)
5. Here you have to create the initial admin user to get started. Please use the value _admin_ both as username and as password, 
then click the button _Create_. Please note: In production you must use a much more secure password for the admin user!
6. Now you can continue to the _Administration Console_ by clicking on the corresponding link displayed and login using the new credentials

![Keycloak Init](keycloak_initial_admin.png)

If all worked successfully you should see the settings page of the _Workshop_ realm and Keycloak is ready for this Workshop !

#### Startup Keycloak

You only have to do the initial setup section for local install once.
If you have stopped Keycloak and want to start it again then follow the next lines in this section.

To startup [Keycloak](https://keycloak.org):

1. Open a terminal and change directory to sub directory __<KEYCLOAK_INSTALL_DIR>/bin__ and start Keycloak using 
the __standalone.sh__(Linux or Mac OS) or __standalone.bat__ (Windows) scripts
2. Wait until keycloak has been started completely - you should see something like this `...(WildFly Core ...) started in 6902ms - Started 580 of 842 services`

#### Remap default port of Keycloak

In case port _8080_ does not work on your local machine (i.e. is used by another process) then you may have to change Keycloak to use another port.
This can be done like this (e.g. for remapping port to 8090 instead of 8080):

On Linux/MAC:
```
./standalone.sh -Djboss.socket.binding.port-offset=10
```

On Windows:
```
./standalone.bat -Djboss.socket.binding.port-offset=10
```

Note: Take into account that for all URL's pointing to Keycloak in the hands-on steps you always have to use the remapped port
instead of default one (8080) as well. 

### Open Keycloak Admin UI

Independent of the setup type (docker or local install), to access the web admin UI of Keycloak 
you need to perform these steps:

1. Now direct your browser to [localhost:8080/auth/admin](http://localhost:8080/auth/admin/)
2. Login into the admin console using __admin/admin__ as credentials

Now, if you see the realm _workshop_ on the left then Keycloak is ready to use it for this workshop.

![Keycloak Workshop](keycloak_workshop.png)

### Further Information

If you want to know more about setting up a Keycloak server for your own projects 
then please consult the [keycloak administration docs](https://www.keycloak.org/docs/latest/server_admin/index.html).