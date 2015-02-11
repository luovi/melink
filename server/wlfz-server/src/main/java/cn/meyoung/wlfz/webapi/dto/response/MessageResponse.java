package cn.meyoung.wlfz.webapi.dto.response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shaowei
 * on 2014/12/26
 * 信息输出的参数
 * 输出的json字段可以不一样。。
 * 也可以自定义参数字段(当前entity中不存在的)比如 *
 * 属于当前实体entity的字段，名称是一样的就不用再mapping.xml中映射了；如果名称不一样需要映射
 * 输出的字段可以是其他实体中的字段，当前实体不存在的--就需要在entity中关联其他表。。
 * 注意:每个字段都需要生成读写set,get方法。。都需要this.
 */
public class MessageResponse implements Serializable {
    @JSONField(name = "user_id")//输出的json字段名称和entity实体属性不一样时,需要在dozerBeanMapping.xml映射
    private Integer userid;//大小写最好区分开，下面的set,get 最好用this调用，否则容易造成形参=形参
    /**
     * //contact_name关联user表中的联系人姓名,,需要在user的entity中关联user；
     * 可以在mapping.xml中添加字段映射；那么controll中add,list方法输出Resoponse字段的时候就不用单独
     * 在去**service.find(id**)获取set了。
     * 也可以不用映射:类似needgood,那么就需要在controll的add,list方法中通过**service.find(id**)获取set
     * 手动通过set,get的方式给entity负值，而不是beenmapper.map映射
     */
    private String contact_name;


    private String Mobile;
    private String Message;//内容
    private Date Send_time;//默认当前时间
    private Integer Type; //1短信推送，2 jpush，默认为1
    private String Title;//标题，短信推送 可以没有标题
    @JSONField(name = "cargo_id")//输出的json字段名称和属性不一样时
    private Integer cargoid;//货源id,wlfz_cargo.id //--本身表有的字段最好和entity中的一致


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


    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public Integer getCargoid() {
        return cargoid;
    }

    public void setCargoid(Integer cargoid) {
        this.cargoid = cargoid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
