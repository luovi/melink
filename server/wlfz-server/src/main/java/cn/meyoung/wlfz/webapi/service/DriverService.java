package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListDriverRequest;
import cn.meyoung.wlfz.webapi.entity.Driver;
import org.springframework.data.domain.Page;

/**
 * Created by goushicai on 14-12-4.
 */
public interface DriverService {
    Driver find(Integer id);


    void save(Driver driver);

    /**
     * 获取司机信息
     *
     * @param userId 用户id
     * @return 司机信息列表 一般只有一个
     */
    Page<Driver> findByUserId(Integer userId);

    /**
     * 获取司机信息
     *
     * @param plateNumber 车牌
     * @return 司机信息列表 一般只有一个
     */
    Page<Driver> findByPlateNumber(ListDriverRequest request);

    Page<Driver> list(ListDriverRequest request);
}
