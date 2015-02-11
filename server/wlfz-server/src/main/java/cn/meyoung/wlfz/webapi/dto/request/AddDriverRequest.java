package cn.meyoung.wlfz.webapi.dto.request;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by goushicai on 14-12-8.
 */
public class AddDriverRequest implements Serializable {
    @NotNull
    private String plate_number;

    @NotNull
    private String name;

    @NotNull
    private String contact_number;

    private String id_number;
    private String license;
    private Byte checked;

    private String memo;

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Byte getChecked() {
        return checked;
    }

    public void setChecked(Byte checked) {
        this.checked = checked;
    }
}
