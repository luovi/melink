package cn.meyoung.wlfz.webapi.dto.request;

import java.io.Serializable;

/**
 * 分页请求参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-24
 */
public class PagedRequest implements Serializable {

    public final static int DEFAULT_PAGE_SIZE = 20;

    private Integer page_no;
    private Integer page_size;

    public Integer getPage_no() {
        return (null == page_no || page_no < 1) ? 1 : page_no;
    }

    public void setPage_no(Integer page_no) {
        this.page_no = page_no;
    }

    public Integer getPage_size() {
        return null == page_size ? DEFAULT_PAGE_SIZE : page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }
}
