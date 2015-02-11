package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListNoticeRequest;
import cn.meyoung.wlfz.webapi.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by shaowei
 * on 2015/1/4.
 */

public interface NoticeService  {


    void Save(Notice message);

    Notice Find(Integer id);

    void Delete(Integer id);

    Page<Notice> List(ListNoticeRequest request);


}
