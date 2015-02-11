package cn.meyoung.wlfz.webapi.jPush;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by goushicai on 14-12-10.
 */
@Component
public class jPushApi {
    private Logger logger = LoggerFactory.getLogger(jPushApi.class);

    @Value("#{settings['jPush.url']}")
    private String jpush_url;

    @Value("#{settings['jPush.appKey']}")
    private String jpush_appKey;

    @Value("#{settings['jPush.masterSecret']}")
    private String jpush_masterSecret;

    public String push(String[] mobile, String title, String message, String cargo_id) {
        String mobilelist = "";
        for (int i = 0; i < mobile.length; i++) {

            if (mobile[i].trim() != "" && !mobile[i].equals(null)) {
                mobilelist += ",\"" + mobile[i] + "\"";
            }

        }
        mobilelist = mobilelist == "" ? "" : mobilelist.substring(1);

        String param = "{" +
                "\"platform\":\"all\"," +
                "\"audience\":{" +
                "\"alias\":[" + mobilelist + "]" +
                "}," +
                "\"notification\":{" +
                "\"alert\":\"" + message + "\"," +
                "\"title\":\"" + title + "\"," +
                "\"android\" : { \"extras\" : { \"cargo_id\" : \"" + cargo_id + "\"}}," +
                "\"ios\" : { \"extras\" : { \"cargo_id\" : \"" + cargo_id + "\"}}" +
                "}" +
                "}";


        HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30 * 1000);
        client.getHttpConnectionManager().getParams().setSoTimeout(30 * 1000);

        PostMethod postMethod = new PostMethod(jpush_url);
        postMethod.setRequestHeader("Content-Type", "application/json; charset=utf-8");
        String auth = this.getJPush_Authorization();
        postMethod.setRequestHeader("Authorization", auth);
        postMethod.setRequestBody(param);
        String error_msg = "";
        try {
            int page_status = client.executeMethod(postMethod);
            String str = postMethod.getResponseBodyAsString();
            if (page_status == HttpStatus.SC_OK) {
                logger.debug("jPush成功.\r\n传入:{} ,\r\n返回:{} \r\n", param, str);
            } else if (page_status == 400) {
                error_msg = "提交失败,找到不对应的别名";
            } else if (page_status == 401) {
                error_msg = "验证失败";
            } else if (page_status == 405) {
                error_msg = "只支持 HTTP Post 方法";
            } else if (page_status == 500) {
                error_msg = "服务器端内部逻辑错误，请稍后重试。";
            } else if (page_status == 503) {
                error_msg = "内部服务超时，请稍后重试。";
            } else {
                error_msg = "其它错误";
            }
        } catch (Exception e) {
            error_msg = e.getMessage();
            logger.error("jPush成功", e);
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }

        return error_msg;
    }

    private String getJPush_Authorization() {
        String str = Base64.encodeBase64String((jpush_appKey + ":" + jpush_masterSecret).getBytes());
        return "Basic " + str;
    }

    public String getDeviceByAliase(String aliase) {
        HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30 * 1000);
        client.getHttpConnectionManager().getParams().setSoTimeout(30 * 1000);
        String url = "https://device.jpush.cn/v3/aliases/" + aliase + "?platform=android,ios";
        GetMethod method = new GetMethod(url);
        method.setRequestHeader("Accept", "application/json");
        String auth = this.getJPush_Authorization();
        method.setRequestHeader("Authorization", auth);
        String dev_id = "";
        try {
            int page_status = client.executeMethod(method);
            String str = method.getResponseBodyAsString();
            if (page_status == HttpStatus.SC_OK) {
                JSONObject res = JSON.parseObject(str);

                dev_id = res.getJSONArray("registration_ids").get(0).toString();
                logger.debug("jPush获取别名成功.\r\n传入:{} ,\r\n返回:{} \r\n", url, str);
            } else {
                //error_msg = "其它错误";
            }
        } catch (Exception e) {
            //error_msg = e.getMessage();
            logger.error("jPush 获取别名失败", e.getMessage());
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }

        return dev_id;
    }
}
