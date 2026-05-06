package com.bjpowernode.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解
 * 判断在哪些方法下添加数据权限
 *
 * 元meta注解:描述注解的注解
 */
@Documented //生成javadoc的文档
@Target(ElementType.METHOD) //限制该注解在方法上使用
@Retention(RetentionPolicy.RUNTIME)//在运行时该注解生效
public @interface DataScope {

    //表的别名，多表的关联查询时会用到  where tu.id=3       where u.owner_id=2
    String tableAlias(); // tu

    //要过滤的字段名
    String columnName();//id  owner_id
}
