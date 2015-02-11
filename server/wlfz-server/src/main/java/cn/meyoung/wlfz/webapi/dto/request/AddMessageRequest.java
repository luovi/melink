package cn.meyoung.wlfz.webapi.dto.request;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 添加信息的请求参数
 * Created by shaowei
 * on 2014/12/26
 * 由于是输入数据所以要加注解验证数据格式。。
 * 如果有ModityRequest的话，AddRequst可以直接继承与ModityRequest 因为他们的字段几乎一样
 *  /* 这种是实体entity中自定义的变量IS_NOT_CHECKED
 request参数并没有对应entity中的checked,status字段，需要自己给他负值
 car.setChecked(Car.IS_NOT_CHECKED);
 car.setStatus((byte) 1);
 也就是，request参数可以不和entity实体对应，无论从数量还是名称。。;
 其实request的参数内容是要add到表的字段的。。
 如果不一样的名称话，还需要在dozemapping.xml中添加映射
 如果数量不一样的话，entity中某些字段就需要手动set 设置了。。。
 注意:每个字段都需要生成读写set,get方法。。都需要this.
 注意:参数中，只要设置了@notnull和@notblank的都需要传入;没设置的可以不传入也就是默认是null
 可以在controll的request参数方法中判断该参数是否为空，然后在付给默认值。
 integer一般设置@notnull;string一般设置@notblank
 */
public class AddMessageRequest implements Serializable {
    @NotNull//对输入参数的格式内容约束
    @Min(0)
    private Integer user_id;//和实体entity的字段不对应,需要在dozerBeanMapping.xml映射

    @NotBlank
    @Length(max = 11)
    private String Mobile;

    @NotBlank//表示一定要有内容，并且不能是null," "或者空格
    @Length(max = 500)
    private String Message;//内容


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date Send_time;//默认当前时间


    @Range(min = 1, max = 2)
    private Integer Type; //1短信推送，2 jpush，默认为1

    @Length(max = 200)
    private String Title;//标题，短信推送 可以没有标题

    @Min(0)
    private Integer cargo_id;//货源id,wlfz_cargo.id

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
        this.Mobile = mobile;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public Date getSend_time() {
        return Send_time;
    }

    public void setSend_time(Date send_time) {
        this.Send_time = send_time;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        this.Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }
}
