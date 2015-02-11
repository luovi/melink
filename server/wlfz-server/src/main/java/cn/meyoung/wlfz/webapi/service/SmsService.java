package cn.meyoung.wlfz.webapi.service;

/**
 * Created by goushicai on 14-12-3.
 */
public interface SmsService {
    String getToken(String login_name, String password);

    void sendSms(String mobile, String msg);
}
