package cn.meyoung.wlfz.webapi.dto;


/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public class SuccessApiResult extends ApiResult {

    private Object result;

    public SuccessApiResult() {
        super(Boolean.TRUE);
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
