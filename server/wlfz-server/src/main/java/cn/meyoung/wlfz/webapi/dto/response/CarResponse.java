package cn.meyoung.wlfz.webapi.dto.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 车辆信息
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class CarResponse implements Serializable {
    private static final long serialVersionUID = 7022622066767070102L;

    private Integer id;

    @JSONField(name = "plate_number")
    private String plateNumber;

    @JSONField(name = "locate_number")
    private String locateNumber;

    private String license;

    @JSONField(name = "model_1")
    private String model1;

    @JSONField(name = "model_2")
    private String model2;

    @JSONField(name = "model_3")
    private String model3;

    @JSONField(name = "model_4")
    private String model4;
    private BigDecimal length;
    private BigDecimal tonnage;
    private BigDecimal volume;
    private BigDecimal height;
    private BigDecimal width;
    private String memo;
    private String original;

    @JSONField(name = "original_code")
    private String originalCode;
    private BigDecimal longitude;
    private BigDecimal latitude;

    @JSONField(serialize = false, deserialize = false)
    private Double distance;
    private Byte status;

    @JSONField(name = "user_id")
    private Integer userId;

    private String app_dev_id;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


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

    public String getModel1() {
        return model1;
    }

    public void setModel1(String model1) {
        this.model1 = model1;
    }

    public String getModel2() {
        return model2;
    }

    public void setModel2(String model2) {
        this.model2 = model2;
    }

    public String getModel3() {
        return model3;
    }

    public void setModel3(String model3) {
        this.model3 = model3;
    }

    public String getModel4() {
        return model4;
    }

    public void setModel4(String model4) {
        this.model4 = model4;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getTonnage() {
        return tonnage;
    }

    public void setTonnage(BigDecimal tonnage) {
        this.tonnage = tonnage;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginalCode() {
        return originalCode;
    }

    public void setOriginalCode(String originalCode) {
        this.originalCode = originalCode;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @JSONField(serialize = true, deserialize = false, name = "distance")
    public Double getDistanceForDisplay() {
        if (null != distance) {
            DecimalFormat df = new DecimalFormat("0.0");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return new Double(df.format(distance).toString());
        }

        return distance;
    }

    public String getApp_dev_id() {
        return app_dev_id;
    }

    public void setApp_dev_id(String app_dev_id) {
        this.app_dev_id = app_dev_id;
    }
}
