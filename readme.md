#### 项目介绍
本项目是我在做一个mybatis的项目时，因为之前都是使用hibernate（code first）,写好entity会自动建表的，而mybatis（db first）无法自动建表，所以就网上搜有没有现成的解决方案，
还真搜到了[一个](https://github.com/sunchenbin/A.CTable-Frame),但是原作者的项目中添加了基本的增删改查的mapper，而且对sql的执行强依赖mybatis，我就想能不能通过裸跑的sql的方式，
对不使用mybatis的项目也做下支持，就这样这个项目诞生了。
#### 使用方法
1. 添加依赖
    ```
     <dependency>
        <groupId>com.wyy</groupId>
        <artifactId>actable</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    ```
1. 配置属性    
    ```
    actable.database.type：数据库的类型，暂仅支持mysql
    actable.model.pack：要扫描的需要建表的包路径，多个包可以使用逗号或者分号隔开
    actable.create.mode：建表的模式 none:不做修改，create：删掉旧表重新建表，update：更新表结构
    actable.execute.sql：是否要在项目启动时执行sql
    actable.print.sql:是否打印sql语句
    ```
#### 说明  
本项目只在springboot环境下测试通过，spring+springmvc请自行测试和适配。  
开发环境下可以将自动执行sql打开，生产环境建议关闭，采用手动执行项目内打印的sql的方式执行。

#### 测试结果
1. 建表
1. 建index
1. 建unique
1. 设置默认值
1. 添加字段
1. 删除字段
1. 添加index
1. 删除index
1. 添加unique
1. 删除unique
1. 修改字段长度
1. 修改nullable
1. 修改默认值
1. 修改小数位数
1. 修改注释

