package cn.meyoung.wlfz.webapi.dto.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 登录返回信息
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = -4426329171300052256L;

    private String token;

    @JSONField(name = "user_id")
    private Integer userId;

    @JSONField(name = "is_admin")
    private Byte isAdmin;

    @JSONField(name = "user_name")
    private String userName;

    @JSONField(name = "code")
    private String code;

    private String company;

    @JSONField(name = "contact_number")
    private String contactNumber;

    @JSONField(name = "contact_name")
    private String contactName;

    private String address;

    private String client;

    private List<Car> cars;

    private Map<String, String> settings;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }


    public static class Car implements Serializable {
        private static final long serialVersionUID = 4788731092490321742L;

        private Integer id;

        @JSONField(name = "plate_number")
        private String plateNumber;

        @JSONField(name = "locate_number")
        private String locateNumber;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public String getLocateNumber() {
            return locateNumber;
        }

        public void setLocateNumber(String locateNumber) {
            this.locateNumber = locateNumber;
        }
    }
}
