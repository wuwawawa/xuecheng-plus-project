# 项目介绍

本项目是本公司自研的一个专门针对成人职业技能教育的网络课堂系统，网站提供了成人职业技能培训的相关课程，如：软件开发培训、职业资格证书培训、成人学历教育培训等课程。项目基于B2B2C的业务模式，培训机构可以在平台入驻、发布课程，运营人员对发布的课程进行审核，审核通过后课程才可以发布成功，课程包括免费和收费两种形式，对于免费课程可以直接选课学习，对于收费课程在选课后需要支付成功才可以继续学习。

# 项目地址

https://github.com/wuwawawa/xuecheng-plus-project

# 组织结构

```
xuecheng-plus-project
├── api-test --项目接口测试文件夹
├── xuecheng-plus-auth -- 认证授权模块
├── xuecheng-plus-base -- 工具类及通用代码模块
├── xuecheng-plus-checkcode -- 验证码生成模块
├── xuecheng-plus-content -- 内容管理模块
├── xuecheng-plus-gateway -- 网关模块
├── xuecheng-plus-generator -- 代码生成器
├── xuecheng-plus-learning -- 选课学习模块
├── xuecheng-plus-media -- 媒资管理服务
├── xuecheng-plus-message-sdk -- 消息SDK
├── xuecheng-plus-orders -- 订单管理模块
├── xuecheng-plus-parent -- 所有项目都依赖的父工程
├── xuecheng-plus-search -- 搜索模块
├── xuecheng-plus-system -- 系统模块
└── xxl-job-2.3.1 -- 分布式任务调度中心
```

<img src="https://lmy-picbed.oss-cn-hangzhou.aliyuncs.com/blog/pSaRyvV.png" alt="img" style="zoom:67%;" />



# 技术选型

## 后端技术

| 技术                   | 说明               |
| ---------------------- | ------------------ |
| Spring Cloud           | 微服务框架         |
| Spring Boot            | 容器+MVC框架       |
| Spring Security Oauth2 | 认证和授权框架     |
| MyBatis                | ORM框架            |
| MyBatisGenerator       | 数据层代码生成     |
| Elasticsearch          | 搜索引擎           |
| Redis                  | 分布式缓存         |
| Docker                 | 应用容器引擎       |
| Druid                  | 数据库连接池       |
| Swagger                | 在线api文档        |
| XXL-job                | 分布式任务调度中心 |
| FreeMarker             | 模板引擎技术       |
| Log4j2                 | 日志框架           |
| MinIO                  | 对象存储           |
| JWT                    | JWT登录支持        |
| Lombok                 | 简化对象封装工具   |

## 前端技术

| 技术       | 说明             |
| ---------- | ---------------- |
| Vue        | 前端框架         |
| Vue-router | 路由框架         |
| Vuex       | 全局状态管理框架 |
| Axios      | 前端HTTP框架     |

## 开发环境

| 工具          | 版本号 |
| ------------- | ------ |
| JDK           | 1.8    |
| Mysql         | 8.0.27 |
| Redis         | 7.0    |
| Nginx         | 1.23.3 |
| Elasticsearch | 7.12.1 |
| Nacos         | 2.2.2  |

# 搭建步骤

本项目的前端页面部分在Nginx，并在配置文件中进行了反向代理。管理页面由Vue搭建。整个项目前端搭建起来比较麻烦。所以这里只做部分后端部分接口的演示。



1. 启动`GatewayApplication`网关服务和`AuthApplication`认证授权服务。在项目下api-test文件夹找到xc-auth-api.http，认证授权服务测试接口，设置`Run with dev`。运行已经编写好的密码登录测试接口。将返回的Token复制保存，下面请求别的接口都要带上Token，否则会报401未认证授权。

   <img src="https://lmy-picbed.oss-cn-hangzhou.aliyuncs.com/blog/image-20230526101727165.png" alt="image-20230526101727165" style="zoom: 50%;" />

2. 启动`ContentApplication`内容管理服务。在项目下api-test文件夹找到xc-ontent-api.http，内容管理服务测试接口。在`Authorization: Bearer`后粘贴刚才生成的Token。请求课程查询接口。

   <img src="https://lmy-picbed.oss-cn-hangzhou.aliyuncs.com/blog/image-20230526102840546.png" alt="image-20230526102840546" style="zoom: 33%;" />

搜索服务因需要启动Elatisearch，云服务器内存吃紧，故就不在这里演示。



# 基础设施搭建

## 服务器配置

本项目使用到了一台2核(vCPU) 2 GiB的阿里云服务器来搭建项目所使用到的一些中间件，例如MySQL、Redis和Nacos等。

<img src="https://lmy-picbed.oss-cn-hangzhou.aliyuncs.com/blog/image-20230524214623939.png" alt="image-20230524214623939" style="zoom: 50%;" />



在搭建环境时，需要对服务器进行一些必要的配置。

1. 设置安全组，对于docker容器映射的端口，服务器必须也将端口开放，否则无法访问端口。在docker启动容器时，避免不必要的暴露端口，比如：3306数据库的常用端口。我在服务器3306端口启动MySQL，且密码设置的过于简单，就被黑客给端口扫描了，将数据库表都删除了。为了避免这种情况，key避免不必要的暴露端口，比如：3306、6379、8080等一些常用的端口，修改常用的端口：比如3306修改为3307,6379修改6380等等，提高密码复杂度。

   <img src="https://lmy-picbed.oss-cn-hangzhou.aliyuncs.com/blog/5dd1005be37797b1ab944d26ed72dbe2.png" alt="5dd1005be37797b1ab944d26ed72dbe2" style="zoom:67%;" />

2. 修改防火墙

3. 安装docker

下面就是使用安装一些项目使用到的中间件。

## MySQL

1. 拉取镜像

安装mysql8.0.27

```bash
docker pull mysql:8.0.27
```

2. 配置文件

创建要挂载的文件夹

```bash
mkdir -p /opt/docker/mysql8/conf/
mkdir -p /opt/docker/mysql8/logs/
mkdir -p /opt/docker/mysql8/data/
```

为了拷贝一份配置文件，先随便启动一个镜像

```java
docker run -p 3306:3306 --name mysql8 -e MYSQL_ROOT_PASSWORD=123456  -d mysql:8.0.27
```

启动成功后，运行下面命令拷贝配置文件，到宿主主机

```bash
docker cp  mysql8:/etc/mysql /opt/docker/mysql8/conf
```

删除刚才的容器，重新创建容器，先停止容器，再删除容器

```bash
docker stop mysql8
docker rm mysql8
```

3. 容器启动命令

```bash
docker run \
-p 3306:3306 \
--name mysql8 \
--privileged=true \
--restart unless-stopped \
-v /opt/docker/mysql8/conf/mysql:/etc/mysql \
-v /opt/docker/mysql8/logs:/logs \
-v /opt/docker/mysql8/data:/var/lib/mysql \
-v /etc/localtime:/etc/localtime \
-e MYSQL_ROOT_PASSWORD=123456 \
-d mysql:8.0.27
```



## Redis

1. 拉取镜像

没有指定版本，默认为最新版本

```bash
docker pull redis
```

2. 创建要挂载的文件夹

```bash
mkdir -p /opt/docker/redis/data
vim /opt/docker/redis/redis.conf
```

redis.conf

```properties
# bind 192.168.1.100 10.0.0.1
# bind 127.0.0.1 ::1
#bind 127.0.0.1

protected-mode no
port 6379
tcp-backlog 511
requirepass 000415
timeout 0
tcp-keepalive 300
daemonize no
supervised no
pidfile /var/run/redis_6379.pid
loglevel notice
logfile ""
databases 30
always-show-logo yes
save 900 1
save 300 10
save 60 10000
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes
dbfilename dump.rdb
dir ./
replica-serve-stale-data yes
replica-read-only yes
repl-diskless-sync no
repl-disable-tcp-nodelay no
replica-priority 100
lazyfree-lazy-eviction no
lazyfree-lazy-expire no
lazyfree-lazy-server-del no
replica-lazy-flush no
appendonly yes
appendfilename "appendonly.aof"
no-appendfsync-on-rewrite no
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
aof-load-truncated yes
aof-use-rdb-preamble yes
lua-time-limit 5000
slowlog-max-len 128
notify-keyspace-events ""
hash-max-ziplist-entries 512
hash-max-ziplist-value 64
list-max-ziplist-size -2
list-compress-depth 0
set-max-intset-entries 512
zset-max-ziplist-entries 128
zset-max-ziplist-value 64
hll-sparse-max-bytes 3000
stream-node-max-bytes 4096
stream-node-max-entries 100
activerehashing yes
hz 10
dynamic-hz yes
aof-rewrite-incremental-fsync yes
rdb-save-incremental-fsync yes
```





## Nacos

1. 拉取镜像

```bash
docker pull nacos/nacos-server
```

2. 创建数据库

https://github.com/alibaba/nacos/blob/master/config/src/main/resources/META-INF/nacos-db.sql

mysql新建nacos的数据库,，并执行下面的SQL脚本

```sql
CREATE DATABASE nacos;
```

```sql
/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info   */
/******************************************/
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) DEFAULT NULL,
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(20) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL,
  `c_use` varchar(64) DEFAULT NULL,
  `effect` varchar(64) DEFAULT NULL,
  `type` varchar(64) DEFAULT NULL,
  `c_schema` text,
  `encrypted_data_key` text NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_aggr   */
/******************************************/
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_beta   */
/******************************************/
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(20) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_info_tag   */
/******************************************/
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(20) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = config_tags_relation   */
/******************************************/
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = group_capacity   */
/******************************************/
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = his_config_info   */
/******************************************/
CREATE TABLE `his_config_info` (
  `id` bigint(64) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL,
  `group_id` varchar(128) NOT NULL,
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL,
  `md5` varchar(32) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` text,
  `src_ip` varchar(20) DEFAULT NULL,
  `op_type` char(10) DEFAULT NULL,
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   数据库全名 = nacos_config   */
/*   表名称 = tenant_capacity   */
/******************************************/
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE users (
	username varchar(50) NOT NULL PRIMARY KEY,
	password varchar(500) NOT NULL,
	enabled boolean NOT NULL
);

CREATE TABLE roles (
	username varchar(50) NOT NULL,
	role varchar(50) NOT NULL,
	constraint uk_username_role UNIQUE (username,role)
);

CREATE TABLE permissions (
    role varchar(50) NOT NULL,
    resource varchar(512) NOT NULL,
    action varchar(8) NOT NULL,
    constraint uk_role_permission UNIQUE (role,resource,action)
);

INSERT INTO users (username, password, enabled) VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', TRUE);

INSERT INTO roles (username, role) VALUES ('nacos', 'ROLE_ADMIN');
```

3. 配置文件

创建挂载目录，用于映射到容器，目录按自己的情况创建

新建logs目录

 修改配置文件

```bash
mkdir -p /opt/docker/nacos/logs/                      
mkdir -p /opt/docker/nacos/init.d/         
vim /opt/docker/nacos/init.d/custom.properties       
```

修改配置文件custom.properties

```properties
server.contextPath=/nacos
server.servlet.contextPath=/nacos
server.port=8848
 
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://8.130.68.59:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=123456
 
nacos.cmdb.dumpTaskInterval=3600
nacos.cmdb.eventTaskInterval=10
nacos.cmdb.labelTaskInterval=300
nacos.cmdb.loadDataAtStart=false
management.metrics.export.elastic.enabled=false
management.metrics.export.influx.enabled=false
server.tomcat.accesslog.enabled=true
server.tomcat.accesslog.pattern=%h %l %u %t "%r" %s %b %D %{User-Agent}i
nacos.security.ignore.urls=/,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.ico,/console-fe/public/**,/v1/auth/login,/v1/console/health/**,/v1/cs/**,/v1/ns/**,/v1/cmdb/**,/actuator/**,/v1/console/server/**
nacos.naming.distro.taskDispatchThreadCount=1
nacos.naming.distro.taskDispatchPeriod=200
nacos.naming.distro.batchSyncKeyCount=1000
nacos.naming.distro.initDataRatio=0.9
nacos.naming.distro.syncRetryDelay=5000
nacos.naming.data.warmup=true
nacos.naming.expireInstance=true
```

4. 启动命令

启动后可访问

http://8.130.68.59:8848/nacos/index.html

用户名：nacos

密码：nacos

```bash
docker  run \
--name nacos -d \
-p 8848:8848 \
--privileged=true \
--restart=always \
-e JVM_XMS=256m \
-e JVM_XMX=256m \
-e MODE=standalone \
-e PREFER_HOST_MODE=hostname \
-v /mydata/nacos/logs:/home/nacos/logs \
-v /mydata/nacos/init.d/custom.properties:/home/nacos/init.d/custom.properties \
nacos/nacos-server
```

最终容器启动结果：

![image-20230524214809575](https://lmy-picbed.oss-cn-hangzhou.aliyuncs.com/blog/image-20230524214809575.png)





















