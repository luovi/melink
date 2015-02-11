package cn.meyoung.wlfz.webapi.validator;

import cn.meyoung.wlfz.webapi.validator.impl.MefpMode4Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = MefpMode4Validator.class)
public @interface MefpModel4 {

    String message() default "厂商品牌代码不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean requied() default false;

}
