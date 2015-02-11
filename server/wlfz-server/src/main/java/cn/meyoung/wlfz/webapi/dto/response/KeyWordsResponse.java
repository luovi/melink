package cn.meyoung.wlfz.webapi.dto.response;

import java.io.Serializable;

/**
 * Created by Thinkpad on 2015/1/6.
 */
public class KeyWordsResponse implements Serializable {
    private Integer count;
    private String keyword;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
