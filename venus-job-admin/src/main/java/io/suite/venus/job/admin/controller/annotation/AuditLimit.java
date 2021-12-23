package io.suite.venus.job.admin.controller.annotation;


import io.suite.venus.job.admin.enums.OperaCodeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 审核限制
 */
public @interface AuditLimit {
    /**
     * 操作码
     * @return
     */
    OperaCodeEnum code() ;
}
