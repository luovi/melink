package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shaowei
 * on 2014/12/26
 * 查询列表的请求参数;包括分页;请求的参数不是必须的，也就是不必设置notnull,notblank
 * 字段不需要mapping.xml映射转换，只是查询用的参数条件。。(可以自定义参数字段)
 */
public class ListMessagesRequest extends PagedRequest implements Serializable {

    private Integer user_id;//查询条件不需要映射

    private String Mobile;

    private String Message;//内容

    private Integer cargo_id;//货源id,wlfz_cargo.id查询条件不需要映射

    //send_time字段的范围。。。
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date send_time_from;//时间范围开始
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date send_time_to;//时间范围结束

    public Date getSend_time_from() {
        return send_time_from;
    }

    public void setSend_time_from(Date send_time_from) {
        this.send_time_from = send_time_from;
    }

    public Date getSend_time_to() {
        return send_time_to;
    }

    public void setSend_time_to(Date send_time_to) {
        this.send_time_to = send_time_to;
    }


    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }
}
