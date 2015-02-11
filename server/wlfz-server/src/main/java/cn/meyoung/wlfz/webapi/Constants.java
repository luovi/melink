package cn.meyoung.wlfz.webapi;

import java.io.IOException;
import java.util.Properties;

/**
 * 常量定义
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public final class Constants {

    /**
     * 当前用户Token在Request里的名称
     */
    public final static String CURRENT_TOKEN = "CURRENT_TOKEN";

    /**
     * 当前用户Token在HttpHeader里的名称
     */
    public final static String CURRENT_TOKEN_HEADER = "token";


    /**
     * 客户端类型：系统
     */
    public final static String CLIENT_SYSTEM = "system";

    /**
     * 用户代码前缀KEY
     */
    public final static String USER_CODE_PREFIX_KEY = "user_code_prefix";

    /**
     * 货物默认过期时间KEY
     */
    public final static String CARGO_EXPIRED_TIME_KEY = "expired_time";

    /**
     * Token默认过期时间KEY
     */
    public final static String TOKEN_EXPIRED_TIME_KEY = "token_expired_time";


    public static Properties ErrCode;
    public static Properties Model1;
    public static Properties Model2;
    public static Properties Model3;
    public static Properties Model4;
    public static Properties Car;

    static {
        try {
            ErrCode = new Properties();
            ErrCode.load(Constants.class.getResourceAsStream("/errorCode.properties"));

            Model1 = new Properties();
            Model1.load(Constants.class.getResourceAsStream("/model1.properties"));

            Model2 = new Properties();
            Model2.load(Constants.class.getResourceAsStream("/model2.properties"));

            Model3 = new Properties();
            Model3.load(Constants.class.getResourceAsStream("/model3.properties"));

            Model4 = new Properties();
            Model4.load(Constants.class.getResourceAsStream("/model4.properties"));

            Car = new Properties();
            Car.load(Constants.class.getResourceAsStream("/car.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
