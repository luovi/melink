package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.mefp.MeFPApi;
import cn.meyoung.wlfz.webapi.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by goushicai on 14-12-3.
 */
@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private MeFPApi meFPApi;

    public String getToken(String login_name, String password) {

        return "";
    }

    public void sendSms(String mobile, String msg) {
        return;
    }
}
