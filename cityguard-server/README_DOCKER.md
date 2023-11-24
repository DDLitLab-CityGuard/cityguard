1. Pull the Postgres Image from Dockerhub

   ``` docker pull postgres ```
2. Make sure your application.yml file contains the postgres databank container name which you will start in step 5

   ``` spring:
  datasource:
    url: jdbc:postgresql://cityguard.isa.uni-hamburg.de:5432/postgres
    username: postgres
    password: **```
4. Create the Backend Image

    ``` docker build ```
5. Create a Docker Network to allow communication between postgres container and backend container

   ```docker network create mynetwork```
6. First run the postgres container with the networkname and portnumber

   ``` docker run --name pgdb --network mynetwork -p 5432:5432 -d -e POSTGRES_PASSWORD=<POSTGRES_PASSWORD> -d postgres```
7. Next run the backend container

    ``` docker run --name server --network mynetwork -p 5123:5123 -d  cityguard```
