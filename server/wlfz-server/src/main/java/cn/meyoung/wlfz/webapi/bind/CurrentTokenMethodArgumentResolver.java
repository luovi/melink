package cn.meyoung.wlfz.webapi.bind;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class CurrentTokenMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {


        CurrentToken currentToken = parameter.getParameterAnnotation(CurrentToken.class);

        return webRequest.getAttribute(currentToken.value(), NativeWebRequest.SCOPE_REQUEST);
    }
}
