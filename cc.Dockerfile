FROM node:18-alpine3.17 as build
LABEL CONFIG_LOCATION=/cityguard-client/config/config.js
COPY cityguard-client /cityguard-client

WORKDIR /cityguard-client

RUN npm install
RUN npm run build

FROM nginx:latest
COPY --from=build /cityguard-client/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx","-g","daemon off;"]