# 模拟论坛的问答平台项目（持续更新）


## 技术栈
工具：IDEA，Centos7服务器，Navicat。

- SpringBoot
- BootStrap
- MySQL
- MyBatis
- Redis

## 目录
- [项目的基本框架及配置](#项目的基本框架及配置)
- [AOP与IOC](#AOP和IOC)
- [数据库创建与Mybatis集成](#数据库创建与Mybatis集成)

## 项目的基本框架及配置
使用IDEA创建SpringBoot项目，生成基于maven的项目。

使用pom.xml添加web，aop，devtools等库的依赖

创建本地git仓库，与github进行连接。可以直接使用IDEA的git工具进行操作，感觉方便又强大

## AOP和IOC

IOC 解决对象实例化以及依赖传递问题，解耦。
    
AOP 解决纵向切面问题，主要实现日志和权限控制功能。
    
Aspect 实现切面，并且使用 logger 来记录日志，用该切面的切面方法来监听 Controller。

## 数据库创建与Mybatis集成