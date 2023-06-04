# Chitragupta

Chitragupta is a toy system for testing hypothesis related to customer user journey analysis.

Current objective is to build capability to estimate the number of user completing a customer user journey (CUJ) in a given time period where ordering of events matters (like strict funnel analysis).

## Setup
1. Install java17 and set env var JAVA_17_HOME.
2. Install maven.
3. Install docker

## Build and run
1. `cd docker`
2. Build docker images using `bash build_images.sh`, this triggers mvn clean package internally
3. Run using docker componse `docker-compose up`

## Pending items
1. Write a batch/spark job to calculate exact number of users completing a CUJ.
2. Compare the result of the above spark job with intersection outputs from druid.
3. replace println with proper logging statements