#!/bin/sh
set -e
host1="$1"
shift
until mysql -h "$host1" -u app -ppass -e 'quit'; do
  >&2 echo "Mysql is unavailable - sleeping"
  sleep 1
done
>&2 echo "Mysql is up - verifying Node"
host2="$1"
shift
until curl "$host2"; do
  >&2 echo "Node is unavailable - sleeping"
  sleep 1
done
>&2 echo "Node is up - starting app-shop"
cmd="$@"
exec $cmd