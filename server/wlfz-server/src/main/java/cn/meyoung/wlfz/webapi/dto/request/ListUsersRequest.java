package cn.meyoung.wlfz.webapi.dto.request;

/**
 * 获取用户列表参数
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-24
 */
public class ListUsersRequest extends PagedRequest {

    private String user_name;
    private String company;
    private Byte checked;
    private Byte status;
    private String keyword;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Byte getChecked() {
        return checked;
    }

    public void setChecked(Byte checked) {
        this.checked = checked;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
