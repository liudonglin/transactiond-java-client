package com.liudonglin.transactiond.tr.core.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LcnTransaction {

    /**
     * 分布式事务传播行为
     *
     * @return 传播行为
     * @see Propagation
     */
    Propagation propagation() default Propagation.REQUIRED;
}