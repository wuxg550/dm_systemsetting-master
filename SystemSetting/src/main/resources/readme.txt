${project.name}-${version}.${git.commit.id.abbrev}-${build.time}.tar.gz

soft_description:
运行参数配置服务，该分支屏蔽掉对接验证中心，不需要登录验证，无操作日志。

install_message:
config目录下application.properties文件可配置
zookeeper服务地址 
  zookeeper.address
mysql数据库地址
  spring.datasource.url
