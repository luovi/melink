package cn.meyoung.wlfz.webapi.dto.request;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by goushicai on 14-11-26.
 */
public class ListUploadfileRequest extends PagedRequest implements Serializable {
    private Integer id;
    private Integer user_id;
    private String file_type;
    private Integer file_size_from;
    private Integer file_size_to;
    private Date upload_time_from;
    private Date upload_time_to;
    private Integer status;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public Integer getFile_size_from() {
        return file_size_from;
    }

    public void setFile_size_from(Integer file_size_from) {
        this.file_size_from = file_size_from;
    }

    public Integer getFile_size_to() {
        return file_size_to;
    }

    public void setFile_size_to(Integer file_size_to) {
        this.file_size_to = file_size_to;
    }

    public Date getUpload_time_from() {
        return upload_time_from;
    }

    public void setUpload_time_from(Date upload_time_from) {
        this.upload_time_from = upload_time_from;
    }

    public Date getUpload_time_to() {
        return upload_time_to;
    }

    public void setUpload_time_to(Date upload_time_to) {
        this.upload_time_to = upload_time_to;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
