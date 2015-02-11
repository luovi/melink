package cn.meyoung.wlfz.webapi.dto;

import cn.meyoung.wlfz.webapi.Constants;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
public final class ApiResultUtils {

    private static final ApiResult SUCCESS = new ApiResult(Boolean.TRUE);

    /**
     * 成功
     *
     * @return
     */
    public static ApiResult Success() {
        return SUCCESS;
    }

    /**
     * 成功并带返回值
     *
     * @param result
     * @return
     */
    public static ApiResult Success(Object result) {
        SuccessApiResult apiResult = new SuccessApiResult();
        apiResult.setSuccess(Boolean.TRUE);
        apiResult.setResult(result);
        return apiResult;
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static ApiResult Error(String code, String message) {
        return new ErrorApiResult(code, message);
    }

    public static ApiResult Error(String code) {
        String message = Constants.ErrCode.getProperty(code, "");
        return new ErrorApiResult(code, message);
    }

    /**
     * 参数错误
     *
     * @return
     */
    public static ApiResult ErrorParameter() {
        return Error("104");
    }

    /**
     * 无操作权限
     *
     * @return
     */
    public static ApiResult NoPermission() {
        return Error("108");
    }

    /**
     * 无效用户
     *
     * @return
     */
    public static ApiResult NonUser() {
        return Error("105");
    }

    /**
     * 无效车辆
     *
     * @return
     */
    public static ApiResult NonCar() {
        return Error("603");
    }

    /**
     * 无效货物
     *
     * @return
     */
    public static ApiResult NonCargo() {
        return Error("605");
    }


    /**
     * 分页数据
     *
     * @param list
     * @param pageNo
     * @param pageSize
     * @param total
     * @return
     */
    public static ApiResult Paged(Iterable list, int pageNo, int pageSize, long total) {
        PagedApiResult apiResult = new PagedApiResult();
        apiResult.setResult(list);
        apiResult.setPageNo(pageNo + 1);
        apiResult.setPageSize(pageSize);
        apiResult.setTotal(total);
        return apiResult;
    }

    /**
     * 无效通话记录
     *
     * @return
     */
    public static ApiResult NonCalllog() {

        return Error("999", "找不到符合条件的通话数据");
    }

    /**
     * 无效信息message
     *
     * @return
     */
    public static ApiResult NonMessage() {
        return Error("999","找不到该条信息");
    }

    public static ApiResult NonNotice() {
        return Error("999","找不到该条内容");
    }

    public static ApiResult NonLocation() {
        return Error("999","找不到该条记录");
    }

    /**
     * 无效交易记录
     *
     * @return
     */
    public static ApiResult NonTrade() {

        return Error("999", "找不到符合条件的交易数据");
    }

    /**
     * 无效上传文件
     *
     * @return
     */
    public static ApiResult NonUploadfile() {

        return Error("999", "找不到符合条件的文件");
    }

    /**
     * 无效上传文件
     *
     * @return
     */
    public static ApiResult UploadfileChecked() {

        return Error("999", "原有的文件已通过审核，不能再次上传");
    }

    /**
     * 无效要货信息
     *
     * @return
     */
    public static ApiResult NonNeedGoods() {

        return Error("999", "找不到符合条件的要货信息");
    }

}
