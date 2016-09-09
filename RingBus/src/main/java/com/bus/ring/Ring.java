package com.bus.ring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by handgunbreak@gmail.com on 16/7/4.
 * Mail: handgunbreak@gmail.com
 * Copyright: 杭州医本健康科技有限公司(2016)
 * Description:
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Ring {

    //过滤关键字
    String filter() default RingConstants.RingFilter;

    //分发策略
    DispatchPolicy policy() default DispatchPolicy.DefaultPolicy;
}
