package com.liudonglin.transactiond.tr.core.config;

import com.liudonglin.transactiond.tr.core.TCAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(value = {TCAutoConfiguration.class, DependenciesImportSelector.class})
public @interface EnableDistributedTransaction {
}
