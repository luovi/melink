package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListLocationRequest;
import cn.meyoung.wlfz.webapi.entity.Location;
import org.springframework.data.domain.Page;

/**
 * Created by shaowei on 2015/1/4.
 */
public interface LocationService {
    Location Find(Integer id);
    Page<Location> List(ListLocationRequest request);
}
