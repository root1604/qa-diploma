#!/bin/bash
sudo docker ps -a | awk '{print $NF}' | tail -n +2 |  while read line ; do sudo docker rm -fv $line ; done
sudo docker rmi $(sudo docker images -a -q)
cd ./artifacts/mysql-version
sudo docker-compose up -d
