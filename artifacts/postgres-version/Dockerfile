FROM centos:7.7.1908
WORKDIR /opt/app
COPY postgres-version/application.properties .
COPY ./aqa-shop.jar .
COPY ./waitForPostgresAndNode.sh .
RUN chmod +x waitForPostgresAndNode.sh
RUN yum -y install java
RUN yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm
RUN yum install -y postgresql12; yum clean all
CMD ["./waitForPostgresAndNode.sh","postgres-for-shop","node-for-shop:9999","java","-jar","aqa-shop.jar"]
EXPOSE 8080