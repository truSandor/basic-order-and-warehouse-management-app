#!/bin/sh
docker run --name cont_psql_oaw_db-e POSTGRES_DB=oaw_db -e POSTGRES_USER=person -e POSTGRES_PASSWORD=person --network oaw_network -d postgres:alpine3.17