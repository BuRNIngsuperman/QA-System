# 模拟论坛的问答平台项目（持续更新）


## 技术栈
工具：IDEA，Centos7服务器，Navicat。

- SpringBoot
- BootStrap
- MySQL
- MyBatis
- Redis
- Solr

## 目录
- [项目的基本框架及配置](#项目的基本框架及配置)
- [AOP与IOC](#AOP和IOC)
- [数据库创建与Mybatis集成](#数据库创建与Mybatis集成)
- [注册登录登出模块](#注册登录登出模块)
- [问题发布及敏感词过滤](#问题发布及敏感词过滤)
- [评论中心和站内信模块](#评论中心和站内信模块)
- [点赞和点踩模块](#点赞和点踩模块)


## 项目的基本框架及配置
使用IDEA创建SpringBoot项目，生成基于maven的项目。

使用pom.xml添加web，aop，devtools等库的依赖

创建本地git仓库，与github进行连接。可以直接使用IDEA的git工具进行操作，感觉方便又强大

## AOP和IOC

IOC 解决对象实例化以及依赖传递问题，解耦。
    
AOP 解决纵向切面问题，主要实现日志和权限控制功能。
    
Aspect 实现切面，并且使用 logger 来记录日志，用该切面的切面方法来监听 Controller。

## 数据库创建与Mybatis集成

使用Mysql创建用户数据表和提出的问题数据表

SpringBoot使用Mybatis进行数据库表的操作和映射

编写首页前端，将用户数据和绑定的问题数据输出到前端展示

## 注册登录登出模块

### 注册

1. 用户名和密码合法性验证
2. 密码salt加密，密码强度检测(md5库)

### 登录

1. 服务器密码校验/三方校验回调，token登记 

   1.1 服务器端token关联userid 
   1.2 客户端存储token(app存储本地，浏览器存储cookie)

2. 服务端/客户端token有效期设置（记住登陆）

3. 拦截器设置，根据cookie中的ticket获取访问的用户信息

### 登出

设置cookie中ticket状态为1，view渲染登出处理

> 为什么不使用session共享数据，而使用数据表来共享token/ticket

这样做可以使访问多个服务器时都可以共享数据，获取用户数据的信息。

### 未登录跳转

借助拦截器我们可以对请求进行分析，如果当前访问的用户未登录，就进行重定向到登录注册页面
同时携带当前页面请求信息，可以使登录注册完成之后直接跳转到要访问的页面


## 问题发布及敏感词过滤

增加问题发布和查看页面，在问题发布时增加敏感词的过滤替代

防止 xss 注入直接使用 HTMLutils 的方法即可实现

根据敏感问题文本构建**字典树模型**处理敏感词，替换敏感词。将该过滤作为一个服务，让需要需要过滤敏感词调用该服务。

## 评论中心和站内信模块

评论中心和站内信模块都是比较简单就能实现的模块，主要就是要注意数据库表单的设计

消息的逻辑是，两个用户之间发送一条消息，有一个唯一的会话 id，这个会话里可以有多条这两个用户的交互信息。通过一个用户 id 获取该用户的会话列表，再根据会话 id 再获取具体的会话内的多条消息。

在页面中设计评论和站内信分页

## 点赞和点踩模块
在评论中设置对评论的点赞和点踩操作，这个模块主要是基于Redis实现的

我们需要根据业务封装好对于Jedis操作的Service

根据需求确定 key 字段，格式是——like：实体类型：实体id 和 dislike：实体类型：实体 id。这样可以将喜欢一条新闻的人存在一个集合，不喜欢的存在另一个集合。通过统计数量可以获得点赞和点踩数。

一般点赞点踩操作是先修改 Redis 的值并获取返回值，然后再异步修改 MySQL 数据库的 likecount 数值。这样既可以保证点赞操作快速完成，也可保证数据一致性。

