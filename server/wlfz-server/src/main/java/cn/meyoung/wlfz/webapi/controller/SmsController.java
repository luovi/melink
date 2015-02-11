package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddSmsRequest;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.mefp.MeFPApi;
import cn.meyoung.wlfz.webapi.utils.RequestUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by goushicai on 14-12-3.
 */
@RestController
@RequestMapping(value = "/mefp")
public class SmsController extends BaseController {

    /**
     * 最后获取的token
     */
    private String lastToken;

    /**
     * token失效的时间
     */
    private Date tokenExpiredTime;

    @Autowired
    private MeFPApi meFPApi;


    @Value("#{settings['mefp.smsApi']}")
    private String smsApi;


    @RequestMapping(value = "/sms", method = RequestMethod.POST)
    public ApiResult addTrade(@Valid AddSmsRequest req, Errors errors, @CurrentToken UserToken token) throws Exception {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }
        if (req.getMobiles().trim() == "" || req.getMobiles().split(",").length == 0) {
            return ApiResultUtils.Error("999", "接收手机不能为空");
        }
        if (req.getMessage().trim() == "") {
            return ApiResultUtils.Error("999", "发送的消息不能为空");
        }

        String[] mob = req.getMobiles().split(",");
        String result = meFPApi.sendSms(smsApi, mob, req.getMessage());
        if (result == "") {
            //短信发送成功;mob可能有多个号码批量发送
            for (int i = 0; i < mob.length; i++) {

                try {
                    addMessage(req.getUser_id().toString(), mob[i].toString(), req.getMessage(), "", "0"
                            , "/message");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return ApiResultUtils.Success();
        } else {
            return ApiResultUtils.Error("999", result);
        }

    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ApiResult getAccount(@CurrentToken UserToken token) {

        if (token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        JSONObject account = meFPApi.accessVerify();
        account.remove("token");
        return ApiResultUtils.Success(account);
    }


    /**
     * 记录发送日志消息
     * Server:server+/message
     */
    private static boolean addMessage(String user_id, String Mobile, String Message,
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
        params.put("Type", "1");//1 短信 2 jpush
        params.put("Title", Title);//短信没有标题
        params.put("cargo_id", cargo_id == null ? "0" : cargo_id);

        //授权token;内部调用就不需要token了;外部客户端调用需要

        HttpURLConnection conn = (HttpURLConnection) RequestUtils.sendPostRequest(Server,
                params,
                null);
        if (null == conn) {
            // "连接ｕｒｌ网络失败");
            return false;
        } else {

            InputStream inStream = conn.getInputStream();
            flag = RequestUtils.parseJSONCargoEvaluation(inStream);

            if (!flag) {

                return false;

            } else {
                return true;
            }

        }
    }


}
