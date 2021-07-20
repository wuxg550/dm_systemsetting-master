FROM 192.168.2.34:5000/jdk:1.8.0_212

ADD /target/SystemSetting-*-prod.tgz /home/iotmp/

EXPOSE 8905



WORKDIR /home/iotmp/SystemSetting

ENTRYPOINT ["bin/SystemSetting_Docker.sh","start"]