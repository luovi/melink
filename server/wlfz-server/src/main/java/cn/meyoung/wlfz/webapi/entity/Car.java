package cn.meyoung.wlfz.webapi.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * 车辆信息
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Entity
@Table(name = "wlfz_cars")
@DynamicUpdate
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)//多对1关系添加缓存
/*@Cache(usage = CacheConcurrencyStrategy.READ_ONLY,region="mycache")
@Cacheable(true)*/
public class Car extends BaseEntity<Integer> {

    /**
     * 客服确认：已确认
     */
    public static final byte IS_CHECKED = 1;

    /**
     * 客服确认：未确认
     */
    public static final byte IS_NOT_CHECKED = 0;

    private Integer userId;
    private String license;
    private String plateNumber;
    private String locateNumber;
    private String model1;
    private String model2;
    private String model3;
    private String model4;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal height;
    private BigDecimal volume;
    private BigDecimal tonnage;
    private Byte status;
    private Byte checked;
    private String memo;
    private String original;
    private String originalCode;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Double distance;
    private String app_dev_id;

    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "license", length = 50)
    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    @Column(name = "plate_number", length = 50)
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Column(name = "locate_number", length = 50)
    public String getLocateNumber() {
        return locateNumber;
    }

    public void setLocateNumber(String locateNumber) {
        this.locateNumber = locateNumber;
    }

    @Column(name = "model_1", length = 50)
    public String getModel1() {
        return model1;
    }

    public void setModel1(String model1) {
        this.model1 = model1;
    }

    @Column(name = "model_2", length = 50)
    public String getModel2() {
        return model2;
    }

    public void setModel2(String model2) {
        this.model2 = model2;
    }

    @Column(name = "model_3", length = 50)
    public String getModel3() {
        return model3;
    }

    public void setModel3(String model3) {
        this.model3 = model3;
    }

    @Column(name = "model_4", length = 50)
    public String getModel4() {
        return model4;
    }

    public void setModel4(String model4) {
        this.model4 = model4;
    }

    @Column(name = "length")
    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    @Column(name = "width", precision = 8, scale = 2)
    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    @Column(name = "height", precision = 8, scale = 2)
    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    @Column(name = "volume", precision = 8, scale = 2)
    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Column(name = "tonnage", precision = 8, scale = 2)
    public BigDecimal getTonnage() {
        return tonnage;
    }

    public void setTonnage(BigDecimal tonnage) {
        this.tonnage = tonnage;
    }

    @Column(name = "status")
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Column(name = "checked")
    public Byte getChecked() {
        return checked;
    }

    public void setChecked(Byte checked) {
        this.checked = checked;
    }

    @Column(name = "memo", length = 500)
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "original", length = 50)
    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    @Column(name = "longitude", precision = 15, scale = 10)
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    @Column(name = "latitude", precision = 15, scale = 10)
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Column(name = "original_code", length = 50)
    public String getOriginalCode() {
        return originalCode;
    }

    public void setOriginalCode(String originalCode) {
        this.originalCode = originalCode;
    }

    @Transient
    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getApp_dev_id() {
        return app_dev_id;
    }

    public void setApp_dev_id(String app_dev_id) {
        this.app_dev_id = app_dev_id;
    }
}
