package cn.meyoung.wlfz.webapi.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-23
 */
public class PagedApiResult extends SuccessApiResult {

    @JSONField
    private Long total;

    @JSONField(name = "page_no")
    private Integer pageNo;

    @JSONField(name = "page_size")
    private Integer pageSize;

    public PagedApiResult() {
        super();
    }


    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
