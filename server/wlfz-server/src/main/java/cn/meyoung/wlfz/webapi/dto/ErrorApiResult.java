package cn.meyoung.wlfz.webapi.dto;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public class ErrorApiResult extends ApiResult {

    private Error error;

    public ErrorApiResult() {
        super();
        this.setSuccess(Boolean.FALSE);
    }

    public ErrorApiResult(String code, String message) {
        this();
        this.error = new Error();
        this.error.setCode(code);
        this.error.setMessage(message);
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    /**
     * 错误信息
     */
    public class Error {
        /**
         * 错误代码
         */
        private String code;

        /**
         * 错误消息
         */
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
