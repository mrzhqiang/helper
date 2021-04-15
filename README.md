# helper

[![Release](https://jitpack.io/v/mrzhqiang/helper.svg)](https://jitpack.io/#mrzhqiang/helper)

`Java API` 总让人陷入选择困难，即便老手也会在浩瀚如海的 `JavaDoc` 中迷失自我，更不用说那些新手们。

一个活生生的例子就是，`java.util.Date` 类对时间格式化非常不友好，并且由于它并非 `final`
——不可变类，所以总导致一些莫名其妙的问题，比如多线程格式化异常，另外还有很多设计上的缺陷，例如日期时间混合问题、时区问题、国际化问题，这些缺陷在 《Effective Java》 中也有提及。

为了避免这一类问题，节省编写模板代码的时间，`helper` 库由此诞生。

## 简介

- [x] helper：JVM 辅助工具，项目依赖包含 [Guava][1] 库（谷歌开源的辅助工具），[Config][2] 库（类型安全的配置文件工具）。
- [x] helper-dependencies：项目依赖模块，包含本项目所有模块的依赖，懒人必备。
- [x] helper-parent：顶级模块，其他模块都挂在这里。
- [x] helper-javafx：JavaFX 辅助工具，利用 [RxJavaFx][3] 库实现 MVVM 架构更方便。
- [x] helper-data：提供数据相关辅助工具，使用 [Guice][5] 依赖注入实例。
    - [x] helper-data-api：定义基本的数据接口，比如 `Repository`、`Pageable` 等。
    - [x] helper-data-redis：简化 [Jedis][4] 的使用。
    - [x] helper-data-ebean：简化 [EBean][6] 的使用（主要基于 MySQL 数据库）。
    - [x] helper-data-cassandra：简化 [Cassandra-driver-core][7] 的使用。
    - [x] helper-data-elasticsearch：简化 Elastic Search 客户端 [rest][8] 的使用。
- [ ] helper-sample：各个模块的功能样例。

## 使用

1. 访问 [jitpack.io][9]，添加本仓库的 URL 以获取依赖。
2. 参考 [helper-sample][10] 模块，获取更详细的使用方法。

## 计划

### 样例
更丰富的使用场景演示。

### 工具库
- 【文档处理模块】Excel 表格、Word 文档、PDF 文档：简历、报表、文件
- 【支付集成】：支付宝、微信、第三方及第四方支付
- 【IM 集成】：xmpp 协议、腾讯云 IM、极光推送 IM
- 【短信集成】：暴风短信、阿里云短信、腾讯云短信、网易云短信、华为云短信、极光短信、第三方短信
- 【直播集成】：三体云语音视频聊天、极光语音视频聊天、腾讯云视频直播
- 【视频点播】：腾讯云短视频、OSS 对象存储付费功能

# 鸣谢
感谢 [JetBrains 开源开发许可证](https://www.jetbrains.com/community/opensource/#support) 为本项目提供的支持。

# Licenses

```
   Copyright 2021 mrzhqiang

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

[1]:https://mvnrepository.com/artifact/com.google.guava/guava

[2]:https://mvnrepository.com/artifact/com.typesafe/config

[3]:https://mvnrepository.com/artifact/io.reactivex.rxjava2/rxjavafx

[4]:https://github.com/mrzhqiang/helper/tree/master/helper

[5]:https://mvnrepository.com/artifact/redis.clients/jedis

[6]:https://mvnrepository.com/artifact/io.ebean/ebean

[7]:https://mvnrepository.com/artifact/com.datastax.cassandra/cassandra-driver-core

[8]:https://mvnrepository.com/artifact/org.elasticsearch.client/rest

[9]:https://jitpack.io/#mrzhqiang/helper

[10]:https://github.com/mrzhqiang/helper/tree/master/helper-sample
