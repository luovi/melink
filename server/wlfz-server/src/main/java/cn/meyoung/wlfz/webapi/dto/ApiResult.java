package cn.meyoung.wlfz.webapi.dto;

import java.io.Serializable;

/**
 * API返回结果
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public class ApiResult implements Serializable {


    private static final long serialVersionUID = -6132058665845444115L;

    private Boolean success;

    public ApiResult() {

    }

    public ApiResult(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
