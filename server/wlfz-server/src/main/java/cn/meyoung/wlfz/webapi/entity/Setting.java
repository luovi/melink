package cn.meyoung.wlfz.webapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 客户端配置
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Entity
@Table(name = "wlfz_settings")
public class Setting extends BaseEntity<Integer> {
    private String client;
    private String key;
    private String value;

    @Column(name = "client", length = 50)
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @Column(name = "key", length = 50)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Column(name = "value", length = 200)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
