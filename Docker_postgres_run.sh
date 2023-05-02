#!/bin/sh
docker run --name cont_psql_oaw_db -p 5433:5432 -e POSTGRES_DB=oaw_db -e POSTGRES_USER=person -e POSTGRES_PASSWORD=person --network oaw_network -d postgres:alpine3.17
# -p valszeg nem kell