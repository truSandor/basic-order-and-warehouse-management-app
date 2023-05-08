#!/bin/sh
docker run --name cont_oaw_app -p 80:8080 -e DB_HOST=cont_psql_oaw_db -e DB_PORT=5432 -e DB_USER=person -e DB_PASSWORD=person --network oaw_network -rm -d img_oaw_app