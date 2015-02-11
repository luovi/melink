package cn.meyoung.wlfz.webapi.dto.response;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by goushicai on 14-11-3.
 */
public class GetCargoCountResponse implements Serializable {
    private long count;
    private Date cur_time;

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCur_time() {
        return cur_time;
    }

    public void setCur_time(Date cur_time) {
        this.cur_time = cur_time;
    }
}
