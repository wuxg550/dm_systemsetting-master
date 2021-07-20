SystemSetting项目

docker 启动方式

(1) docker pull 192.168.2.34:5000/system_setting:1.0.0

（2）宿主机上新建config目录,并拷贝application.properties和logback-spring.xml文件到config下

（3）创建并启动镜像,使用宿主机上的config目录下配置文件来运行

docker run -p 8905:8905 -d
-v /home/iotmp/systemsetting/config/:/home/iotmp/SystemSetting/config 
-v /home/iotmp/systemsetting/logs:/home/iotmp/SystemSetting/logs 
-v /etc/localtime:/etc/localtime:ro 
192.168.2.34:5000/system_setting:1.0.0

①　第一个-v ：宿主机的配置文件夹config映射到容器内的配置文件夹中，前者覆盖后者。宿主机config目录下必须要有全部的配置文件
②　第二个-v：宿主机的logs文件夹映射到容器内的logs文件夹，前者覆盖后者。日志输出后，在宿主机logs会实时看到
③　第三个-v：用宿主机的时间来同步容器内的时间，保持2者一致
如有permission deny问题：加上--privileged=true ，让容器真正拥有root权限