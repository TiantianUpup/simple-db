# simple-db
### 项目简介
伯克利大学的cs186课程作业，实现数据库的核心功能  
附：[课程地址](https://sites.google.com/site/cs186fall2013/homeworks/project-1)
### cs186-proj1
cs186-proj1的第一个exerise就是实现`Tuple.java`和`TupleDesc.java`。`Tuple`翻译成中文就是元组，在关系型数据库中有两个比较重要的名词，1是元组，2是属性。  数据库中的每一行就是一个元组，数据库中的每一列就是一个属性，属性分为型属性和值属性，型属性是指字段的类型和取值范围，比如`int`,`bigint`这些，值属性就是存储的值。元组一般由多个属性组成。`Tuple.java`描述的就是元组这个名词，`TupleDesc.java`和`filed`集合组成了属性，`TupleDesc.java`只定义了字段名和字段类型，字段值存储在`Tuple.java`的`field`集合中
### cs186-proj2
### cs186-proj3
### cs186-proj4
