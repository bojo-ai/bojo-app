#!/usr/bin/env bash

export APP_GROUP="ai.bojo"
export CIRCLE_SHA1=$(git rev-parse HEAD)

./gradlew dockerTag

export DOCKER_NET="${APP_GROUP}-${CIRCLE_SHA1}-net"
docker network create -d bridge ${DOCKER_NET}

docker run \
    --rm \
    --detach \
    --env "SPRING_PROFILES_ACTIVE=h2" \
    --name "app" \
    --network=${DOCKER_NET} \
    -p 8080:8080 \
    "${APP_GROUP}/app:$CIRCLE_SHA1"

sleep 10

# Check if all container are running
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}\t{{.Networks}}"

# It's not possible to use volume mounting with the docker executor in CircleCI.
# So we create a dummy container which will hold a volume with config, copy our
# postman files into this volume, and start the newman runner using this volume.
docker create -v /etc/newman --name "newman-config-${CIRCLE_SHA1}" --network=${DOCKER_NET} alpine:3.4 /bin/true
docker cp ./postman/ai.bojo.postman_collection.json "newman-config-${CIRCLE_SHA1}":/etc/newman
docker cp ./postman/docker.postman_environment.json "newman-config-${CIRCLE_SHA1}":/etc/newman
docker run --rm --volumes-from "newman-config-${CIRCLE_SHA1}" --name newman --network=${DOCKER_NET} --rm postman/newman \
  run "/etc/newman/ai.bojo.postman_collection.json" \
  --environment="/etc/newman/docker.postman_environment.json"

docker kill "app"
docker network rm ${DOCKER_NET}

