package com.bjpowernode.aspect;

import com.bjpowernode.annotation.DataScope;
import com.bjpowernode.entity.TRole;
import com.bjpowernode.entity.TUser;
import com.bjpowernode.query.BaseQuery;
import com.bjpowernode.util.LoginInfoUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据权限切面类
 */
@Aspect
@Component
public class DataScopeAspect {

    /**
     * 环绕通知 在有@DataScope注解的方法上执行
     * 1.获取方法上DataScope注解中的属性值 tableAlias  columnName
     * 2.获取用户的角色
     * 3.若角色不是管理员，则拼接sql条件
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "@annotation(com.bjpowernode.annotation.DataScope)")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {

        //1.获取方法上DataScope注解中的属性值 tableAlias  columnName
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        //获取方法上的注解
        DataScope dataScope = methodSignature.getMethod().getAnnotation(DataScope.class);

        String tableAlias = dataScope.tableAlias();//别名
        String columnName = dataScope.columnName();//字段名

        //2.获取用户的角色
        TUser currentLoginUser = LoginInfoUtil.getCurrentLoginUser();
        List<TRole> tRoleList = currentLoginUser.getTRoleList();

        //3.若角色不是管理员，则拼接sql条件
        //判断角色是否为管理员
        boolean flag = tRoleList
                .stream() //将集合转成stream流
                .anyMatch(tRole -> tRole.getRole().equals("admin"));

        if(!flag){
            //拼接sql条件 拼接条件到baseQuery中
            //获取方法的第一个参数
            Object params = joinPoint.getArgs()[0];

            if (params instanceof BaseQuery) {
                BaseQuery baseQuery = (BaseQuery)params;

                // select * from t_user tu where 1=1 and tu.id=2
//                String sql = """
//                        and ${tableAlis}.${columnName}=currentLoginUser.getId()
//                        """;
                baseQuery.setFilterSQL(" and " + tableAlias + "." + columnName + "=" + currentLoginUser.getId());

            }

        }

        Object proceed = joinPoint.proceed();
        return proceed;

    }
}
