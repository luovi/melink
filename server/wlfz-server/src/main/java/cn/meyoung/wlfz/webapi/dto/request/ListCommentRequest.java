package cn.meyoung.wlfz.webapi.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论列表参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class ListCommentRequest extends PagedRequest implements Serializable {

    private static final long serialVersionUID = 7188442276473900787L;

    private Integer user_id;
    private Integer car_id;
    private Integer cargo_id;

    private Integer comment_userid;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date comment_time_from;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date comment_time_to;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getComment_userid() {
        return comment_userid;
    }

    public void setComment_userid(Integer comment_userid) {
        this.comment_userid = comment_userid;
    }

    public Date getComment_time_from() {
        return comment_time_from;
    }

    public void setComment_time_from(Date comment_time_from) {
        this.comment_time_from = comment_time_from;
    }

    public Date getComment_time_to() {
        return comment_time_to;
    }

    public void setComment_time_to(Date comment_time_to) {
        this.comment_time_to = comment_time_to;
    }

    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }
}
