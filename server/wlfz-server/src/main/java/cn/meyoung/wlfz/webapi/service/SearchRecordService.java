package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.KeyWords;
import cn.meyoung.wlfz.webapi.dto.request.ListNoticeRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListSearchRecordRequest;
import cn.meyoung.wlfz.webapi.entity.SearchRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Created by shaowei on 2015/1/5.
 */
public interface SearchRecordService  {

    void Save(SearchRecord message);

    Page<SearchRecord> List(ListSearchRecordRequest request);

    /**
     * 查找最热门的count条搜索记录
     * 分组查询
     * @param
     * @return
     */
    List<KeyWords> getKeyWordGroup(int max, int whichpage);

    List getKeyWordList(int max, int whichpage);

    List getKeyWordByCount();

}
