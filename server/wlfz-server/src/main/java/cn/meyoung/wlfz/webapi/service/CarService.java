package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListCarLocationsRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCarsRequest;
import cn.meyoung.wlfz.webapi.entity.Car;
import cn.meyoung.wlfz.webapi.entity.Location;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 车辆信息服务
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public interface CarService {

    /**
     * 获取车辆信息
     *
     * @param id 车辆id
     * @return 车辆信息
     */
    Car find(Integer id);

    /**
     * 获取用户的车辆信息
     *
     * @param userId 用户id
     * @return 车辆信息列表
     */
    List<Car> findByUserId(Integer userId);

    /**
     * 修改车辆信息
     *
     * @param car 车辆信息
     */
    void save(Car car);


    /**
     * 车牌是否已经存在
     *
     * @return true：存在 false：不存在
     */
    boolean existsCarByPlateNumber(String plateNumber);


    /**
     * 获取车辆列表
     *
     * @param request 参数
     * @return 车辆列表
     */
    Page<Car> list(ListCarsRequest request);

    /**
     * 修改车辆位置
     *
     * @param car      位置信息
     * @param location 位置信息
     */
    void modifyLocation(Car car, Location location);


    /**
     * 获取车辆位置列表
     *
     * @param car     车辆
     * @param request 参数
     * @return 车辆列表
     */
    Page<Location> findCarLocations(Car car, ListCarLocationsRequest request);
}
