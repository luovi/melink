package cn.meyoung.wlfz.webapi.bind;

import cn.meyoung.wlfz.webapi.Constants;

import java.lang.annotation.*;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentToken {

    /**
     * 当前token在request中的名称
     *
     * @return
     */
    String value() default Constants.CURRENT_TOKEN;
}
