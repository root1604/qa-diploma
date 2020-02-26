#!/bin/sh
set -e
host1="$1"
shift
until PGPASSWORD="pass" psql -h "$host1" -U app -c '\q'; do
  >&2 echo "Postgres is unavailable - sleeping"
  sleep 1
done
>&2 echo "Postgres is up - verifying Node"
host2="$1"
shift
until curl "$host2"; do
  >&2 echo "Node is unavailable - sleeping"
  sleep 1
done
>&2 echo "Node is up - starting app-shop"
cmd="$@"
exec $cmd