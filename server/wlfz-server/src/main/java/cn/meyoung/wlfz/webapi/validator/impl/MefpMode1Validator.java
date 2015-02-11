package cn.meyoung.wlfz.webapi.validator.impl;

import cn.meyoung.wlfz.webapi.Constants;
import cn.meyoung.wlfz.webapi.validator.MefpModel1;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class MefpMode1Validator implements ConstraintValidator<MefpModel1, String> {

    private MefpModel1 constraintAnnotation;

    @Override
    public void initialize(MefpModel1 constraintAnnotation) {
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

        return Constants.Model1.containsKey(value);
    }
}
