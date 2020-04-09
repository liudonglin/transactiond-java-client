package com.liudonglin.transactiond.tr.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TccTransaction {

    /**
     * 确认事务执行方法
     * 该方法参数需要和事务单元的参数保持一致
     *
     * @return  确认方法
     */
    String confirmMethod() default "";


    /**
     * 取消事务执行方法
     * 该方法参数需要和事务单元的参数保持一致
     *
     * @return  取消方法
     */
    String cancelMethod() default "";

    /**
     * 分布式事务传播行为
     *
     * @return 传播行为
     * @see Propagation
     */
    Propagation propagation() default Propagation.REQUIRED;
}
