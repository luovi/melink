package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListCalllogRequest;
import cn.meyoung.wlfz.webapi.entity.Calllog;
import org.springframework.data.domain.Page;

/**
 * Created by goushicai on 14-11-4.
 */
public interface CalllogService {
    Calllog find(Integer id);

    void save(Calllog calllog);

    Page<Calllog> list(ListCalllogRequest request);

}
