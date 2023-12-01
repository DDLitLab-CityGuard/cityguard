FROM nginx:latest
COPY cityguard-client /usr/share/nginx/html
EXPOSE 80
