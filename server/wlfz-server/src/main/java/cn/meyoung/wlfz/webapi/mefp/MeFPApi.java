package cn.meyoung.wlfz.webapi.mefp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
@Component
public class MeFPApi {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Logger logger = LoggerFactory.getLogger(MeFPApi.class);

    @Value("#{settings['mefp.key']}")
    private String apiKey;

    @Value("#{settings['mefp.secret']}")
    private String apiSecret;

    @Value("#{settings['mefp.api']}")
    private String apiBaseUrl;

    @Value("#{settings['mefp.login_name']}")
    private String login_name;

    @Value("#{settings['mefp.password']}")
    private String password;

    @Value("#{settings['mefp.suffix']}")
    private String suffix;


    /**
     * 最后获取的token
     */
    private String lastToken;

    /**
     * token失效的时间
     */
    private Date tokenExpiredTime;


    /**
     * 登录验证，获取token
     *
     * @return token
     */
    public JSONObject accessVerify() {
        Map<String, String> params = new HashMap<>();
        params.put("verify_for", "user");
        params.put("login_name", login_name);
        params.put("password", password);
        JSONObject rootJson = doMefpHttpGet("/user/verify", params);
        if (null != rootJson && rootJson.getBooleanValue("success")) {
            JSONObject resultJson = rootJson.getJSONObject("result");
            if (null != resultJson) {
                return resultJson;
            }
        }

        return null;
    }

    /**
     * 用户登录验证
     *
     * @param loginName 用户名
     * @param password  密码
     * @return
     */

    public boolean userVerify(String loginName, String password) {

        Map<String, String> params = new HashMap<>();
        params.put("verify_for", "user");
        params.put("login_name", loginName);
        params.put("password", password);
        JSONObject rootJson = doMefpHttpGet("/user/verify", params);

        if (null == rootJson) {
            return false;
        }

        JSONObject resultJson = rootJson.getJSONObject("result");
        return rootJson.getBooleanValue("success") && resultJson != null && StringUtils.isNotBlank(resultJson.getString("token"));

    }

    /**
     * 调用MeFP发送短信接口
     */
    public String sendSms(String mobile, String msg) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("mobile", mobile);
        params.put("msg", msg);

        JSONObject json = doMefpHttpGet("/sms", params);
        if (null == json) {
            return "999";
        }

        if (json.containsKey("success") && json.getBooleanValue("success")) {
            return "1";
        } else {
            JSONObject error = json.getJSONObject("error");
            return error.getString("code");
        }
    }

    /**
     * 用户注册
     *
     * @param userName      用户名
     * @param password      密码
     * @param company       公司
     * @param contactNumber 手机号码
     * @param captcha       验证码
     * @return
     */
    public String userRegister(String userName, String password, String company, String contactNumber, String captcha) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("username", userName);
        params.put("password", password);
        params.put("company", company);
        params.put("contact_number", contactNumber);
        params.put("captcha", captcha);

        JSONObject json = doMefpHttpGet("/user/register", params);
        if (null == json) {
            return "999";
        }

        if (json.containsKey("success") && json.getBooleanValue("success")) {
            return "1";
        } else {
            JSONObject error = json.getJSONObject("error");
            return error.getString("code");
        }
    }

    /**
     * 发送验证码
     *
     * @param registerContactNumber 手机号码
     * @return 1:发送成功 0:请求失败 613:注册验证码发送失败 616:手机号码已被注册
     */
    public String sendCaptcha(String registerContactNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("token", getToken());
        params.put("register_contact_number", registerContactNumber);

        JSONObject json = doMefpHttpGet("/user/captcha", params);
        if (null == json) {
            return "999";
        }

        if (json.containsKey("success") && json.getBooleanValue("success")) {
            return "1";
        } else {

            JSONObject error = json.getJSONObject("error");
            return error.getString("code");
        }
    }

    /**
     * 获取token
     *
     * @return token
     */
    public String getToken() {

        if (StringUtils.isEmpty(lastToken) || null == tokenExpiredTime || tokenExpiredTime.before(new Date())) {
            lastToken = accessVerify().getString("token");
            tokenExpiredTime = DateUtils.addMinutes(new Date(), 5);
        }

        return lastToken;
    }

    /**
     * 执行mefp请求
     *
     * @param method 请求方法
     * @param params 参数
     * @return
     */
    private JSONObject doMefpHttpGet(String method, Map<String, String> params) {
        String url = apiBaseUrl + method + "?";
        params.put("ts", DATE_FORMAT.format(new Date()));

        try {
            Map<String, String> data = getData(params);
            String param = urlEncode(data);

            String httpGet = doHttpGet(url + param);
            if (StringUtils.isNotEmpty(httpGet)) {
                return JSON.parseObject(httpGet);
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    //发送短信，目前为物流方舟调车使用
    public String sendSms(String url, String[] mobile, String message) {
        String error_msg = "";

        String number = "";
        for (int i = 0; i < mobile.length; i++) {
            number += ",\"" + mobile[i] + "\"";
        }
        number = number == "" ? "" : number.substring(1);
        String param = "{\"route\":\"services_sms_send\",\"numbers\":[" + number + "],\"content\":\"" + (message + suffix) + "\"}";
        String token = this.getToken();
        if (token == null) {
            error_msg = "获取不到MeFP的token，可能是网络请求超时";
            return error_msg;
        }
        HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30 * 1000);
        client.getHttpConnectionManager().getParams().setSoTimeout(30 * 1000);

        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Authorization", token);
        postMethod.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        postMethod.setRequestBody(param);


        try {
            int page_status = client.executeMethod(postMethod);
            if (page_status == HttpStatus.SC_OK) {
                String str = postMethod.getResponseBodyAsString();
                logger.debug("向mefp发送短信成功.\r\n传入:{} ,\r\n返回:{} \r\n", param, str);
                //ApiResultUtils.Success(str);
            } else if (page_status == 400) {
                error_msg = "参数错误或者网络请求超时";
            } else if (page_status == 401) {
                error_msg = "没有权限";
            } else if (page_status == 599) {
                error_msg = "请求超时";
            } else {
                error_msg = "其它错误";
            }
        } catch (Exception e) {
            error_msg = e.getMessage();
            logger.error("向mefp发送短信失败,地址：" + url, e);
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }

        return error_msg;
    }

    /**
     * 执行http请求
     *
     * @param url
     * @return
     */
    private String doHttpGet(String url) {
        HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30 * 1000);
        client.getHttpConnectionManager().getParams().setSoTimeout(30 * 1000);
        GetMethod method = new GetMethod(url);

        try {
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                String backStr = method.getResponseBodyAsString();

                logger.debug("调用mefp成功,状态：{} 地址：{} \r\n返回值：{}\r\n", statusCode, url, backStr);

                return backStr;
            } else {
                logger.error("调用mefp失败,状态：{} 地址：{}", statusCode, url);
            }
        } catch (IOException e) {
            logger.error("调用mefp失败,地址：" + url, e);
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return "";
    }

    private String urlEncode(Map<String, String> params) throws UnsupportedEncodingException {
        Iterator<String> iter = params.keySet().iterator();
        int i = 0;
        StringBuffer sb = new StringBuffer();

        while (iter.hasNext()) {
            String key = iter.next();
            String value = params.get(key);

            if (i != 0) {
                sb.append("&");
            }
            sb.append(key);
            sb.append("=");
            sb.append(URLEncoder.encode(value, "utf-8").toString());

            i++;
        }
        return sb.toString();
    }

    private Map<String, String> getData(Map<String, String> params) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();

        String parameters = Base64.encodeBase64String(urlEncode(params).getBytes());
        String sign = Base64.encodeBase64String(DigestUtils.md5Hex(apiKey + parameters + apiSecret).getBytes());

        result.put("key", apiKey);
        result.put("sign", sign);
        result.put("parameters", parameters);
        return result;
    }

}
