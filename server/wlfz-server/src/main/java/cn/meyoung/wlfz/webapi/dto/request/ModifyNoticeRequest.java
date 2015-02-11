package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shaowei on 2015/1/4.
 * 发布时间不需要修改
 */
public class ModifyNoticeRequest implements Serializable {

    //@NotBlank
    @Length(max = 255)
    private String content;


   // @NotBlank
    @Length(max = 50)
    private String title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
