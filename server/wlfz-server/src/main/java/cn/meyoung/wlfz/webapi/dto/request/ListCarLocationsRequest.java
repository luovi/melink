package cn.meyoung.wlfz.webapi.dto.request;

import java.io.Serializable;

/**
 * 车辆位置列表参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public class ListCarLocationsRequest extends PagedRequest implements Serializable {
    private static final long serialVersionUID = 5766735280569056269L;

    private Byte type;

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
}
