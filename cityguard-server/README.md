# Creating the Backend and Database Containers

1. **Pull the Postgres Image from Dockerhub**

   ```
   docker pull postgres
    ```
3. **Make sure your application.yml file contains the postgres databank container name which you will start in step 5**

   ```
   spring:
      datasource:
         url: jdbc:postgresql://cityguard.isa.uni-hamburg.de:5432/postgres
         username: postgres
         password: <yourpassword>
      jpa:
         database-platform: org.hibernate.dialect.PostgreSQLDialect
         generate-ddl: true
   server:
      port: 5123
   ```
4. **Create the Backend Image**

   ```
    docker build -t <backend_image_name> .
   ```
6. **Create a Docker Network to allow communication between postgres container and backend container**

   ```
   docker network create mynetwork
   ```
7. **First run the postgres container with the networkname and portnumber**
   ```
   docker run --name pgdb --network mynetwork -p 5432:5432 -d -e POSTGRES_PASSWORD=<POSTGRES_PASSWORD> postgres
   ```
8. **Next run the backend container**

   ```
    docker run --name server --network mynetwork -p 5123:5123 -d  cityguard
   ```

# Deleting Images and Container

**Stop the container**:To delte a container you need to stop it first
```
docker stop <container_name_or_id>
```
**Delete the container**
```
docker rm <container_name_or_id>
```
**Delete the image**: Image should not be in usage to get deleted
```
docker rmi <image_name_or_id>
```

# Connecting to the running container 
```
docker exec -it <container_name_or_id> /bin/bash
```
# Opening the logs
```
docker logs --follow my_hitec_django_tool_app
```

