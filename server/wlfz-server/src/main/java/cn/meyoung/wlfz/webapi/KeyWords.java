package cn.meyoung.wlfz.webapi;


/**
 * Created by shaowei on 2015/1/5.
 * 返回关键字分组列表
 * JPQL 支持将查询的属性结果直接作为一个 java class 的构造器参数，并产生实体作为结果返回
 */
public class KeyWords {

    private Long count;
    private String keyword;

    public KeyWords() {
    }

    public KeyWords(Long count, String keyword) {

        this.count = count;

        this.keyword = keyword;

    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
