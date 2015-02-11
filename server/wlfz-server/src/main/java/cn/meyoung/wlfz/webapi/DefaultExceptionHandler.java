package cn.meyoung.wlfz.webapi;

import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
@ControllerAdvice
public class DefaultExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ApiResult processUnauthenticatedException(NativeWebRequest request, Exception e) {
        logger.error("", e);
        return ApiResultUtils.Error("107");
    }
}
