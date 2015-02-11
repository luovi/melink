package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.KeyWords;

import java.util.List;

/**
 * Created by shaowei on 2015/1/6.
 * 自定义方法，实现SearchRecordDao无法定义的方法功能
 *
 */
public interface SearchRecordDaoCustomize {

    public List getKeyWordGroupList(int max, int whichpage);
    public List<KeyWords> getKeyWordGroupPage(int max, int whichpage);
}
