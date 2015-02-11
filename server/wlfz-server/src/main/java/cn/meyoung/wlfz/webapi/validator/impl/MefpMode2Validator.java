package cn.meyoung.wlfz.webapi.validator.impl;

import cn.meyoung.wlfz.webapi.Constants;
import cn.meyoung.wlfz.webapi.validator.MefpModel2;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class MefpMode2Validator implements ConstraintValidator<MefpModel2, String> {

    private MefpModel2 constraintAnnotation;

    @Override
    public void initialize(MefpModel2 constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (constraintAnnotation.requied()) {
            if (StringUtils.isEmpty(value)) {
                return false;
            }
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            }
        }

        return Constants.Model2.containsKey(value);
    }
}
