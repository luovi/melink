package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.request.LoginRequest;
import cn.meyoung.wlfz.webapi.dto.response.UserResponse;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.mefp.MeFPApi;
import cn.meyoung.wlfz.webapi.service.UserService;
import cn.meyoung.wlfz.webapi.utils.AesUtils;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户服务接口
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Value("#{settings['mefp.BaseUri']}")
    public String baseUri;

    @Value("#{settings['mefp.secret']}")
    public String mefp_secret;

    @Value("#{settings['app.secret']}")
    public String app_secret;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult login(@Valid LoginRequest loginReq, Errors errors, HttpServletRequest request) throws Exception {
        MeFPApi mefp = new MeFPApi();

        String req_param = "{\"login_name\":\"13612345678\",\"password\":\"123456\"}";
        //String req_text = AesUtils.encode(req_param, mefp_secret, app_secret);

        String decode = AesUtils.decode("bBqS+E00oJbggzLiPX/6RksiqDYn/lmDTadrVLElHl3xKC+om89qGs/ElpugC3LL",mefp_secret, app_secret);

        //mefp.httpPost(baseUri+"/auth/login",req_text,"melink");

        return ApiResultUtils.Success();
    }

    /**
     * 获取用户信息
     *
     * @param id 用户id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getUser(@PathVariable Integer id) {

        User user = userService.find(id);
        if (null == user) {
            return ApiResultUtils.NonUser();
        }

        UserResponse userRes = BeanMapper.map(user, UserResponse.class);

        return ApiResultUtils.Success(userRes);
    }

    /**
     * 用户注销
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ApiResult logout() {
        return ApiResultUtils.Success();
    }
}
