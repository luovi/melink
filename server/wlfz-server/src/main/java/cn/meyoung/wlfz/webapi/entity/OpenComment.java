package cn.meyoung.wlfz.webapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 开放式评价记录
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Entity
@Table(name = "wlfz_opencomment")
public class OpenComment extends BaseEntity<Integer> {
    private Integer userId;
    private Integer mark;
    private String comment;
    private Integer commentUserId;
    private Date commentTime;
    private Integer car_id;
    private Integer cargo_id;

    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "mark")
    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @Column(name = "comment", length = 500)
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(name = "comment_userid")
    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }

    @Column(name = "comment_time")
    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    @Column(name = "car_id")
    public Integer getCar_id() {
        return car_id;
    }

    public void setCar_id(Integer car_id) {
        this.car_id = car_id;
    }

    @Column(name = "cargo_id")
    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }
}
