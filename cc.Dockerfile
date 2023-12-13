FROM nginx:latest
LABEL CONFIG_LOCATION=/usr/share/nginx/html/config/config.js
COPY cityguard-client /usr/share/nginx/html
EXPOSE 80
