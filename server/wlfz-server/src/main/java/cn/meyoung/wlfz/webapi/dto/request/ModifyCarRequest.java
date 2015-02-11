package cn.meyoung.wlfz.webapi.dto.request;

import cn.meyoung.wlfz.webapi.validator.MefpModel1;
import cn.meyoung.wlfz.webapi.validator.MefpModel2;
import cn.meyoung.wlfz.webapi.validator.MefpModel3;
import cn.meyoung.wlfz.webapi.validator.MefpModel4;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 修改车辆信息参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class ModifyCarRequest implements Serializable {

    private static final long serialVersionUID = -957160245322950096L;

    @Length(max = 50)
    private String plate_number;

    @Length(max = 50)
    private String locate_number;

    @MefpModel1
    @Length(max = 50)
    private String model_1;

    @MefpModel2
    @Length(max = 50)
    private String model_2;

    @MefpModel3
    @Length(max = 50)
    private String model_3;

    @MefpModel4
    @Length(max = 50)
    private String model_4;

    private BigDecimal length;
    private BigDecimal tonnage;
    private BigDecimal volume;
    private BigDecimal height;
    private BigDecimal width;

    @Length(max = 50)
    private String license;

    @Length(max = 500)
    private String memo;

    public String getLocate_number() {
        return locate_number;
    }

    public void setLocate_number(String locate_number) {
        this.locate_number = locate_number;
    }

    public String getModel_1() {
        return model_1;
    }

    public void setModel_1(String model_1) {
        this.model_1 = model_1;
    }

    public String getModel_2() {
        return model_2;
    }

    public void setModel_2(String model_2) {
        this.model_2 = model_2;
    }

    public String getModel_3() {
        return model_3;
    }

    public void setModel_3(String model_3) {
        this.model_3 = model_3;
    }

    public String getModel_4() {
        return model_4;
    }

    public void setModel_4(String model_4) {
        this.model_4 = model_4;
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

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
