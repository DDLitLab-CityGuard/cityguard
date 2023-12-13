FROM postgres:16
ENV POSTGRES_USER admin
ENV POSTGRES_PASSWORD pass
ENV POSTGRES_DB cityguard
COPY cd/ /docker-entrypoint-initdb.d/
EXPOSE 5432/tcp