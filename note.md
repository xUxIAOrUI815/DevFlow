1. 先写后端再写前端
2. 文件夹创建
3. 数据库初始化
4. application.yml 是Spring Boot应用配置文件，用于配置后端服务的基本参数，比如数据库连接和MyBatis-Plus框架行为。
5. 构建通用的
## YAML 文件说明
> 使用缩进表示层级关系，不能用Tab，必须用空格。
> 分为三大块: server Web服务器配置/spring Spring Boot 核心配置/mybatis-plus ORM核心框架

6. 递归查询
    **场景：** 比如有一个大需求叫“完成大作业”，它下面有子任务“后端开发”，子任务下又有孙任务“建表”。
    我们需要一个接口，传入根任务ID，一次性把它的子子孙孙全查出来，并组装成树形结构返回给前端。
    **核心考点：**
    - 数据库层：使用 MySQL 8.0 的 ```WITH RECURSIVE``` 通用表达式 CTE 
    - Java层：如何把数据库查出来的"扁平列表"组装成"树形结构".
---
实现步骤
- 创建 TreeVO 树形视图对象
- 编写 Mapper 注解写递归 SQL
- 编写 Service 组装树形结构
### server Web服务端口
指定SpringBoot内嵌的Tomcat或Netty服务器监听的HTTP端口
默认8080
启动后，API可以通过localhost:8080访问

### spring 应用与数据库配置
应用名称，给服务起一个名字。
用于
- 微服务注册中心识别服务
- 日志输出时标识来源

### 数据库连接配置

1. driver-class-name JDBC驱动类。com.mysql.cj.jdbc.Driver 是 MySQL 8.x 官方驱动（cj = Connector/J）。
2. url 数据库连接字符串，格式：jdbc:mysql://主机:端口/数据库名?参数1=值1&参数2=值2