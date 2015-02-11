package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.PushRequest;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.jPush.jPushApi;
import cn.meyoung.wlfz.webapi.utils.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by goushicai on 14-12-10.
 */
@RestController
@RequestMapping(value = "/jpush")
public class jPushController {
    @Value("#{settings['jPush.defaultTitle']}")
    private String defaultTitle;

    @Autowired
    jPushApi pushApi;

    @RequestMapping(method = RequestMethod.POST)
    public ApiResult jpush(@Valid PushRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }
        //只有管理员角色才能push
        if (token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }
        String[] mobile = req.getMobiles().split(",");
        String error_msg = "";
        String title = defaultTitle;
        if (req.getTitle() != null && req.getTitle().trim() == "") {
            title = req.getTitle();
        }
        error_msg = pushApi.push(mobile, title, req.getMessage(), req.getCargo_id().toString());
        if (error_msg == "") {
            //推送成功记录推送消息日志到message表中
            //短信发送成功;mob可能有多个号码批量发送
            for (int i = 0; i < mobile.length; i++) {

                try {
                    addMessage(req.getUser_id().toString(), mobile[i].toString(), req.getMessage(), title, req.getCargo_id().toString()
                            , "/message");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            return ApiResultUtils.Success();
        } else {
            return ApiResultUtils.Error("999", error_msg);
        }

    }

    @RequestMapping(value = "/device/{aliase}", method = RequestMethod.GET)
    public ApiResult jpush_aliase(@PathVariable String aliase) {

        String re = pushApi.getDeviceByAliase(aliase);
        return ApiResultUtils.Success(re);

    }

    /**
     * 记录发送日志消息
     * Server:server+/message
     */
    private static String addMessage(String user_id, String Mobile, String Message,
                                     String Title, String cargo_id, String Server)
            throws Exception {
        boolean flag = false;

        //判断是否为null  StringUtils.isNotEmpty()
        //判断是否是空白空格 StringUtils.isNotBlank()

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id == null ? "0" : user_id);
        if (StringUtils.isNotBlank(Mobile))
            params.put("Mobile", Mobile);
        else
            params.put("Mobile", "");
        if (StringUtils.isNotBlank(Message))
            params.put("Message", Message);
        else
            params.put("Message", "");
        //不设置该参数的话,controll中addrequest的该参数就是null；需要在
        //addmessage方法加判断然后,设置默认当前时间--前提是send_time的request参数没有注解@notenull,notblank
        // params.put("Send_time", "");
        params.put("Type", "2");//1 短信 2 jpush
        params.put("Title", Title);//可以没有标题
        params.put("cargo_id", cargo_id == null ? "0" : cargo_id);

        //授权token;内部调用就不需要token了;外部客户端调用需要

        String result = RequestUtils.sendPostMethod(Server, params
        );

        return result;
    }

}
