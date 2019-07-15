#!/bin/bash

dockername=$1
dockerport=$2

if [ -z "$dockername" ]; then
   dockername="mongodb_it"
fi

if [ -z "$dockerport" ]; then
    dockerport="27017"
fi

if [ ! "$(docker ps -q -f name=$dockername)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=$dockername)" ]; then
        docker rm $dockername
    fi
    echo "Running docker container $dockername in the localhost port $dockerport"
    docker run --name $dockername -d -p $dockerport:27017 mongo
fi

echo "Starting docker container:"
docker start $dockername

echo "Starting Maven tests..."
mvn test -Dspring.data.mongodb.port=$dockerport
echo "Maven tests finished"

echo "Stopping docker container:"
docker stop $dockername

exit 0
