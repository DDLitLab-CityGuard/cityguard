FROM node:18-alpine3.17 as build
ARG CG_API_URL="http://localhost:8088/api"
ARG GEOCODING_API_URL="https://cityguard.isa.uni-hamburg.de/nominatim"

COPY cityguard-client /cityguard-client

WORKDIR /cityguard-client

RUN echo "export const apiEndpoint = '$CG_API_URL'" > config/config.js
RUN echo "export const geoCoderApiEndpoint = '$GEOCODING_API_URL/search?q='" >> config/config.js
RUN echo "export const geoCoderApiEndpointReverse = '$GEOCODING_API_URL/reverse?format=json&'" >> config/config.js

RUN npm install
RUN npm run build

FROM nginx:latest
COPY --from=build /cityguard-client/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx","-g","daemon off;"]