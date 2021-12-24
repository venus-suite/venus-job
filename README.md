# venus-job

#轻量级分布式任务调度系统

![avatar](doc/logo.png)

## 目录

* [简介](#1)

* [基于源码部署](#2)

* [使用手册](#3)

<h3 id="1">简介</h3>

Venus Job是一个分布式的任务调度平台，主要有以下功能：

* 命名空间管理：对调度任务做命名空间划分，便于管理

* 任务图谱：对所有任务的关系做实时可视化展示

* 调度日志： 提供所有任务调度的执行日志

* 执行器管理: 对任务执行的物理机或者虚拟机做管理，展示执行器的资源占用情况

* 任务管理：对要调度的任务做管理

* 任务审核： 对任务的增删改做审批流，减少任务误操作造成的生产故障。

* 任务大盘：提供任务的整体大盘展示功能

* 用户管理：对系统的用户做管理，同时提供审计日志功能。

Venus Job基于xxl-job 2.0.2，相比xxl-job，主要的增强功能点有：
* 客户端资源监控：提供宿主机的CPU，内存等资源监控信息
* 任务变更审核机制：对任务的增删改做审批流控制
* 操作审计日志: 所有操作皆有记录，便于后续做安全审计

<h3 id="2">基于源码部署</h3>

部署前提条件

* ![](https://img.shields.io/static/v1?label=git&message=2.34.1&color=green&?style=for-the-badge)

* ![](https://img.shields.io/static/v1?label=Node.js&message=12.0&color=green&?style=for-the-badge)

* ![](https://img.shields.io/static/v1?label=Java&message=1.8&color=green&?style=for-the-badge)

* ![](https://img.shields.io/static/v1?label=mysql&message=5.7&color=green&?style=for-the-badge)

Step 1： 下载源码
```
git clone https://github.com/venus-suite/venus-job.git
```
Step 2：编译打包前端代码
```
cd  venus-job-web
npm run build:staging
```

Step 3(可选)：本地调试前端代码
```
npm run dev
```

Step 4：编译打包后端代码

先编辑venus-job-admin\src\main\resources\application.yml 修改mysql连接字符串
接着开始打包：

```
cd venus-job-admin
mvn package 
```

Step 5：运行程序

```
java  -jar venus-job-admin-0.0.1.jar
```

Step 6：浏览器输入 http://localhost:8901 访问

```
java  -jar venus-job-admin-0.0.1.jar
```

<h3 id="3">使用手册</h3>

![avatar](venus-job-web/src/assets/bg.png)

待完善。。。
