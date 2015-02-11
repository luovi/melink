package cn.meyoung.wlfz.webapi.interceptor;

import cn.meyoung.wlfz.webapi.Constants;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.service.UserTokenService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Token拦截器
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Component
public class UserTokenInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserTokenService userTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(Constants.CURRENT_TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            responseTokenError(response);
            return false;
        }

        UserToken userToken = userTokenService.parse(token);
        if (null == userToken ||
                null == userToken.getUserId() ||
                null == userToken.getExpiredTime() ||
                new Date().after(userToken.getExpiredTime())) {
            responseTokenError(response);
            return false;
        }

        request.setAttribute(Constants.CURRENT_TOKEN, userToken);
        return true;
    }

    /**
     * 输出错误信息
     *
     * @param response
     */
    private void responseTokenError(HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try {
            ApiResult apiResult = ApiResultUtils.Error("111");
            response.getWriter().write(JSON.toJSONString(apiResult));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
