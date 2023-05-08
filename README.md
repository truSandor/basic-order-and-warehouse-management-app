# basic-order-and-warehouse-management-app
This project was created as a final project for the "Enterprise backend" course at Codecool.

One instant of the app is currently* running [here](http://129.151.211.224/).

* *probably, unless it crashed, or I shut it down*

## Description

As the title suggest the goal of this project is to create the backend for a stripped down, partial ERP software, that is capable of managing order and warehouse tasks.

The app is a REST API, using Java Spring-boot backend and postgreSQL database.

It can be deployed with the included **docker-compose.yml** file.
 
## Installation

### Docker compose (recommended)

use the included ***docker-compose.yml*** file with the command

> docker compose --env-file variables.env up -d

where ***"variables.env"*** is your environment variable file.

Or on linux just run the ***docker_compose_with_env.sh*** script.

example ***"variables.env"*** file:

    POSTGRES_DB=oaw_db
    POSTGRES_USER=db_user
    POSTGRES_PASSWORD=db_pass
    DB_HOST=db
    DB_PORT=5432
    DB_NAME=${POSTGRES_DB}
    DB_USER=${POSTGRES_USER}
    DB_PASSWORD=${POSTGRES_PASSWORD}

* *note: variables starting with *POSTGRES_*\* are mandatory for postgreSQL container, variables starting with *DB_*\* are needed for the backend. The backend has default values for these environment variables, but the might not match up with the actual db.

### Manual (not recommended)

1) Create a postgre database with the name *"oaw_db"*
2) add a **user with password** and privileges to the db 
Build the project with javac/maven
3) Set up the environment variables in your OS
4) run the .jar file

### Half-Manual (not recommended)

1) create a docker network like:

> docker network create oaw_network

* *note: you can name the network however you want, just use the same name in the following steps*

2) add a postgre container to the network:

> docker run --name cont_psql_oaw_db -p 5433:5432 -e POSTGRES_DB=oaw_db -e POSTGRES_USER=person -e POSTGRES_PASSWORD=person --network oaw_network -d postgres:alpine3.17

* *note: here again you can use different parameters, these are just examples*

3) build the docker image for the backend using the given ***Dockerfile***

> docker build -t img_aow_app .

* *note: you can name the image differently

4) run the backend image

> docker run --name cont_oaw_app -p 80:8080 -e DB_HOST=cont_psql_oaw_db -e DB_PORT=5432 -e DB_USER=person -e DB_PASSWORD=person --DB_NAME=aow_db --network oaw_network -rm -d img_oaw_app

* *note: the -e parameters are the environment variables*

### Environment variables

#### Backend

- **DB_HOST** (default: localhost) : database host address, **not needed if you are using the docker-compose file.**
- **DB_PORT** (default: 5432) : database port, **not needed if you are using the docker-compose file.**
- **DB_NAME** (default: oaw_db): name of the database
- **DB_USER** (default: person) : database user
- **DB_PASSWORD** (default: person) :database user's password

## Endpoints

### landing-controller

- **GET
/**
Shows the landing page with some endpoints.

### storage-unit-controller

- **GET
/storage/{id}**
Gets the storage unit with the given ID.

- **PUT
/storage/{id}**
Updates the storage unit with the given ID

- **DELETE
/storage/{id}**
Deletes the storage unit with the given ID, if it's empty

- **GET
/storage**
Gets all storage units.

- **POST
/storage**
Adds new storage unit to the database. Storage unit is given as Json object in the request body.

- **GET
/storage/component/{component_id}**
Gets all storage units which contains the given Component. (component is given with id.

### product-controller

- **GET
/products/{id}**
Gets the product with the given ID.

- **PUT
/products/{id}**
Updates the product with the given ID

- **DELETE
/products/{id}**
Deletes the product with the given ID, if it doesn't have active order, and it's parts list is empty.

- **GET
/products**
Gets all products which name is containing the given phrase.

- **POST
/products**
Adds new product to the database. Product is given as Json object in the request body.

### parts-list-row-controller

- **GET
/products/partslist/{productId}**
Gets the partsList of the product with the given Id.

- **PUT
/products/partslist/{productId}**
Updates the whole parts list of a product

- **POST
/products/partslist/{productId}**
Adds multiple new lines to a product's parts list. PartsListRows are given as a List of Json objects in the request body.

- **DELETE
/products/partslist/{productId}**
Deletes the whole parts list of a product

- **POST
/products/partslist**
Adds 1 new line to a product's parts list. PartsListRow is given as Json object in the request body.

- **DELETE
/products/partslist/delete/single/row/{id}**
Deletes the PartsListRow with the given ID

### order-controller

- **GET
/orders/{id}**
Gets the order with the given ID.

- **PUT
/orders/{id}**
Updates the order with the given ID

- **DELETE
/orders/{id}**
Deletes the order with the given ID

- **GET
/orders**
Gets all orders where the product's name contains the given phrase.

- **POST
/orders**
Adds new order to the database. Order is given as Json object in the request body.

### component-controller

- **GET
/components/{id}**
Gets the component with the given ID.

- **PUT
/components/{id}**
Updates the component with the given ID

- **DELETE
/components/{id}**
Deletes the component with the given ID

- **GET
/components**
Gets all components which name is containing the given phrase.

- **POST
/components**
Adds new component to the database. Component is given as Json object in the request body.

### Built-in tools

#### Swagger

- /swagger-ui/index.html ([on localhost](http://localhost:8080/swagger-ui/index.html))

#### H2 console

- /h2-console/ ([on localhost](http://localhost:8080/h2-console))