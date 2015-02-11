package cn.meyoung.wlfz.webapi.dto.request;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by goushicai on 14-11-26.
 */
public class AddUploadfileRequest implements Serializable {
    private Integer id;
    @NotNull
    private Integer user_id;

    @NotNull
    private MultipartFile file;

    @NotNull
    private String token;//文件上传接口在head里传token不好操作，将token验证放在参数中
    /*
    *   idcard  身份证
    *   driver  驾照
    *   driving 行驶证
    *   cargo   货源备注信息(语音，记到数据库中)
    *   org     组织代码证
    *   biz     营业执照
    *   file    普通文件
    *
     */
    private String file_type;
    private Integer status;

    private Integer car_id;
    private String memo;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
