FROM centos:7.7.1908
WORKDIR /opt/app
COPY mysql-version/application.properties .
COPY ./aqa-shop.jar .
COPY ./waitForMysqlAndNode.sh .
RUN chmod +x waitForMysqlAndNode.sh
RUN yum -y install java wget
RUN wget https://repo.mysql.com/mysql80-community-release-el7-1.noarch.rpm
RUN yum -y localinstall mysql80-community-release-el7-1.noarch.rpm
RUN wget https://repo.mysql.com/yum/mysql-8.0-community/el/7/x86_64/mysql-community-client-8.0.19-1.el7.x86_64.rpm
RUN yum -y localinstall mysql-community-client-8.0.19-1.el7.x86_64.rpm; yum clean all
CMD ["./waitForMysqlAndNode.sh","mysql-for-shop","node-for-shop:9999","java","-jar","aqa-shop.jar"]
EXPOSE 8080