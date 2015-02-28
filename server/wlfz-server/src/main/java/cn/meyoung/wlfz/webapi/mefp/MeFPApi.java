package cn.meyoung.wlfz.webapi.mefp;

import cn.meyoung.wlfz.webapi.utils.AesUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.omg.CORBA.NameValuePair;
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

    public String httpPost(String url,String encrypt,String app_from) throws Exception{
        String param = "{" +
                "\"encrypt\":\""+encrypt+"\"," +
                "\"app_from\":\""+app_from+"\"}";

        HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30 * 1000);
        client.getHttpConnectionManager().getParams().setSoTimeout(30 * 1000);

        PostMethod postMethod = new PostMethod(url);
        postMethod.setRequestHeader("Content-Type", "application/json; charset=utf-8");

        String error_msg="";
        try{
            postMethod.setRequestBody(param);

            int page_status = client.executeMethod(postMethod);
            String str = postMethod.getResponseBodyAsString();
            if (page_status == HttpStatus.SC_OK) {
                error_msg = str;
            } else {
                error_msg = "其它错误";
            }

        }
        catch (Exception e){
            throw new Exception(e);
        }finally {
            postMethod.releaseConnection();
        }
        return error_msg;
    }

}
