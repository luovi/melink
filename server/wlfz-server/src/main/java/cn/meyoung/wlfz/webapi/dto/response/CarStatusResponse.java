package cn.meyoung.wlfz.webapi.dto.response;

import java.io.Serializable;

/**
 * 车辆状态
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public class CarStatusResponse implements Serializable {
    private static final long serialVersionUID = -3523627863605247216L;

    private Byte status;

    private String desc;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
