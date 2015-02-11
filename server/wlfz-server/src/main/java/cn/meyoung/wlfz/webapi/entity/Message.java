package cn.meyoung.wlfz.webapi.entity;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by shaowei on 2014/12/26.
 * 记录短信和jpush推送成功的日志
 * 注意:每个字段都需要生成读写set,get方法。。都需要this.
 */
@Entity//注解，标准该对象是jpa实体
@Table(name = "wlfz_message")//映射到数据库表
public class Message extends BaseEntity<Integer> {
    //id字段已经在BaseEntity中定义了
    //实体属性可以和数据库表字段不一样，因为下面有column注解
    private User user;//关联之后不用再写当前表的user_id 了
    private String Mobile;
    private String Message;//内容
    private Date Send_time;//默认当前时间
    private Integer Type; //1短信推送，2 jpush，默认为1
    private String Title;//标题，短信推送 可以没有标题
    private Integer Cargo_id;//货源id,wlfz_cargo.id

    //Cargo cargo;//因为关联到  货源Cargo_id．因为直接通过spring-data-jpa操作访问数据库所以有关联的字段可以这么定义

    /**
     * column注解映射到表type字段
     *
     * @return
     */
    @Column(name = "type")
    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        this.Type = type;
    }


    /**
     * column注解映射到表title字段和长度
     *
     * @return
     */
    @Column(name = "title", length = 200)
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }


    /**
     * column注解映射到表send_time字段和长度
     *
     * @return
     */
    @Column(name = "send_time")
    public Date getSend_time() {
        return Send_time;
    }

    public void setSend_time(Date send_time) {
        this.Send_time = send_time;
    }


    /*如果下面做了表关联映射;本身这个可以不要;连同上面的字段声明都不需要*/
    @Column(name = "cargo_id")
    public Integer getCargo_id() {
        return Cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.Cargo_id = cargo_id;
    }

    /**
     * 表字段对应关系，，N对1或1对N或N对N
     * name = "cargo_id"是当前message实体表要关联Cargo表的id字段
     * @return

     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "cargo_id", nullable = true)
     @NotFound(action = NotFoundAction.IGNORE)
     public Cargo getCargo() {
     return cargo;
     }
     public void setCargo(Cargo cargo) {
     this.cargo = cargo;
     }*/

    /**
     * column注解映射到表message字段和长度
     *
     * @return
     */
    @Column(name = "message", length = 500)
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    /**
     * column注解映射到表mobile字段和长度
     *
     * @return
     */
    @Column(name = "mobile", length = 20)
    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        this.Mobile = mobile;
    }


    /**
     * 当前表的user_id关联到user实体的信息
     * response参数的时候要contect_name字段。
     *
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @NotFound(action = NotFoundAction.IGNORE)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
