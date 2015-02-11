package cn.meyoung.wlfz.webapi.mefp;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014-10-15
 */
@Component
public class CarStatusSyncApi {

    private Logger logger = LoggerFactory.getLogger(CarStatusSyncApi.class);

    @Value("#{settings['carStatusSyncUrl']}")
    private String apiUrl;


    /**
     * 	现在系统 0勿扰  1空车 2半载 3满载
     */

    /**
     * 1空车 2满载  4勿扰 5半载
     */
    private static final String[] STATUS = new String[]{"4", "1", "5", "2"};


    @Async
    public void doSync(String tel, Integer status) {
        HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
        client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        client.getHttpConnectionManager().getParams().setSoTimeout(20000);
        PostMethod method = new PostMethod(apiUrl);

        method.addParameter("tel", tel);
        method.addParameter("sms", STATUS[status]);


        try {
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                String backStr = method.getResponseBodyAsString();
                logger.debug("调用车辆状态成功,状态：{} 地址：{} \r\n返回值：{}", statusCode, apiUrl, backStr);

            } else {
                logger.error("调用车辆状态同步失败,状态：{} 地址：{}", statusCode, apiUrl);
            }
        } catch (IOException e) {
            logger.error("调用车辆状态同步失败,地址：" + apiUrl, e);
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
    }
}
