package cn.meyoung.wlfz.webapi.dto.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class CommentResponse implements Serializable {
    private static final long serialVersionUID = 2106018936463076196L;

    @JSONField(name = "user_id")
    private Integer userId;

    private Integer mark;

    private String comment;

    @JSONField(name = "comment_userid")
    private Integer commentUserId;

    @JSONField(name = "comment_time")
    private Date commentTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Integer commentUserId) {
        this.commentUserId = commentUserId;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }
}
