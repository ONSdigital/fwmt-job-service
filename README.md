
[![Build Status](https://travis-ci.org/ONSdigital/fwmt-job-service.svg?branch=master)](https://travis-ci.org/ONSdigital/fwmt-job-service) [![codecov](https://codecov.io/gh/ONSdigital/fwmt-job-service/branch/master/graph/badge.svg)](https://codecov.io/gh/ONSdigital/fwmt-job-service) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/62502b7391f3452ca076e11edf999860)](https://app.codacy.com/project/ONSDigital/fwmt-job-service/dashboard)

# fwmt-job-service
Service to create and reallocate jobs to users

## Running
    ./gradlew bootRun

## Docker
    
    docker run --name jobservice -p:9091:9091 sdcplatform/fwmtjobssvc
    
Or to bring all fwmt services up

    docker-compose up -d

## Docker build

    docker build -t sdcplatform/fwmtjobsvc --build-args jar=PATH_TO_JAR .
    docker push sdcplatform/fwmtjobsvc
    
 ## Copyright
Copyright (C) 2018 Crown Copyright (Office for National Statistics)
