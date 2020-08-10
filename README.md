# dandelion_seed

## 简介

[lihengming/spring-boot-api-project-seed](https://github.com/lihengming/spring-boot-api-project-seed "") 是一个github上基于Spring Boot & MyBatis的种子项目, 非常适合搭建中小型项目RESTful API, 通过代码生成, 只要完成数据库结构, 即可直接生成基础数据操作接口完成代码搭建, 真正实现只关心业务逻辑的开发.

示例代码参考: [dandelion](https://github.com/MlightShadow/dandelion)

该仓库只存放必要的构建, 开箱即用, 只需要配置数据库即可开始体验

## 1 关于整合

### 1.1 框架

[lihengming/spring-boot-api-project-seed](https://github.com/lihengming/spring-boot-api-project-seed "") 是一个非常不错的种子项目, 非常推荐使用, 我对它进行了整合和修改, 去除了一些通用Mapper中的数据库约束, 添加了insert和update的batch操作, 整合还包括了 jwt + 缓存/redis的鉴权, 通过spring-security 获取身份信息 content, spring-cache 缓存, swaggerAPI文档生成等一系列开发所需功能, 当然目前只是用于我个人的喜好, 也行你也会觉得好用.

### 1.2 代码生成

生成部分主要为了方便开发, 做了多种生成模板, 对应不同的数据库表与视图, 生成相关代码, 并且防止覆盖因为业务需求已经修改的生成代码.

## 2 快速上手

### 2.1 文件结构

```none
    |-src
        |-main # 省略了包层次
        |   |-aop # log, 多数据源切面和注解
        |   |-configurer # 配置
        |   |-constant # 常量
        |   |-core # 核心接口
        |   |-dao # 生成的Mapper文件, 一般无需修改
        |   |-dto # restful 接口需要的实体类
        |   |-modal # 根据数据库结构生成的实体类
        |   |-service # 业务逻辑, 生成只包括增改删功能
        |   |-util # 通用工具
        |   |-web # restful Controller
        |   |-resources 配置文件, xml, 其他资源
        |
        |-test # 测试, 生成工具
```

### 2.2 配置数据库

目前支持两种(mssql, mysql)数据库连接, 可以通过多数据源同时使用

生成工具使用的第一步就是配置数据库
`CodeGenerator.java`

```java
    private static final String JDBC_URL = "jdbc:mysql://host:3306/test";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "root";
    private static final String JDBC_DIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
```

```java
    private static final String JDBC_URL = "jdbc:sqlserver://host:1433;databaseName=test";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "sa";
    private static final String JDBC_DIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
```

同样, 配置文件也是

```prop
    spring.datasource.auth.url=jdbc:mysql://host:3306/test?useUnicode=true&characterEncoding=UTF-8
    spring.datasource.auth.username=root
    spring.datasource.auth.password=root
    spring.datasource.auth.driver-class-name=com.mysql.jdbc.Driver
```

```prop
    spring.datasource.auth.url=jdbc:sqlserver://host:1433;databaseName=test
    spring.datasource.auth.username=sa
    spring.datasource.auth.password=sa
    spring.datasource.auth.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

### 2.3 生成代码

从 `CodeGenerator.java` 启动生成器, 输入想要生成的表名即可

### 3.1 部署

可选部署命令,方便输出

```bash
java -jar /work_dir/seed_server.jar > log.out 2>&1 &
```

根目录下 `release.sh` 可以用于发布, `Dockerfile` 可部署至docker容器
