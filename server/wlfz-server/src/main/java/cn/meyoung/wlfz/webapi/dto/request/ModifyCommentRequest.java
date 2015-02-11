package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改评论参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class ModifyCommentRequest implements Serializable {

    private static final long serialVersionUID = 7185322316195114214L;

    @Length(max = 500)
    private String comment;

    @NotNull
    @Range(min = -1, max = 1)
    private Integer mark;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }
}
