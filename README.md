# explain-interception-starter



## 简介

功能：统计一个项目的SQL执行计划以及耗时，并将数据通过http传输到某个接口

实现：利用MyBatis拦截器，拦截正在执行的SQL语句，在sql语句前面加上"explain"进行改写，利用System.currentTimeMillis()进行耗时统计

#### 执行计划

| **列名**      | **含义**                                     |
| ------------- | -------------------------------------------- |
| id            | SELECT查询的序列标识符                       |
| select_type   | SELECT关键字对应的查询类型                   |
| table         | 用到的表名                                   |
| partitions    | 匹配的分区，对于未分区的表，值为 NULL        |
| **type**      | 表的访问方法                                 |
| possible_keys | 可能用到的索引                               |
| **key**       | 实际用到的索引                               |
| key_len       | 所选索引的长度                               |
| ref           | 当使用索引等值查询时，与索引作比较的列或常量 |
| rows          | 预计要读取的行数                             |
| filtered      | 按表条件过滤后，留存的记录数的百分比         |
| Extra         | 附加信息                                     |

#### SQL耗时

```java
long start = System.currentTimeMillis();
// TODO SQL执行
long end = System.currentTimeMillis();
long time = end - start;
```



## 主要功能

- [ ] 拦截正在执行的SQL语句并改写
- [ ] Durid获取SQL所归属的表名、数据库名等信息
- [ ] JdbcTemplate 获取SQL的执行计划并输出
- [ ] RestTemplate 传输数据到指定的接口



## 使用方式

第一步：克隆项目

```
git clone XXXXXXXX.git
```

第二步：clear、install 到本地仓库

第三步：在其它项目中导入jar包

```xml
<dependency>
    <groupId>indi.wgx</groupId>
    <artifactId>explain-interception-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

第四步：配置

在配置文件中加入以下语句

```yml
# 是否开启 本项目 拦截器
explain.interception: true

# 获取项目名称和版本
name: '@project.name@'
version: '@project.version@'
projectId: 286512107663430

# SQL打分项目接口(传输接口)
url-api: http://localhost:8080/init/allData
```



