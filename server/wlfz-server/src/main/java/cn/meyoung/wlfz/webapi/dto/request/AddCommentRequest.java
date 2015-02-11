package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 添加评论参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class AddCommentRequest implements Serializable {
    private static final long serialVersionUID = 2712056240379189239L;
    @NotNull
    @Min(0)
    private Integer user_id;
    private Integer car_id;
    private Integer cargo_id;

    @NotNull
    @Range(min = -1, max = 1)
    private Integer mark;


    @Length(max = 500)
    private String comment;

    @NotNull
    @Min(0)
    private Integer comment_userid;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date comment_time;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getComment_userid() {
        return comment_userid;
    }

    public void setComment_userid(Integer comment_userid) {
        this.comment_userid = comment_userid;
    }

    public Date getComment_time() {
        return comment_time;
    }

    public void setComment_time(Date comment_time) {
        this.comment_time = comment_time;
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
