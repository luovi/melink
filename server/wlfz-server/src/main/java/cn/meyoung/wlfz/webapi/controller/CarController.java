package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.Constants;
import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddCarRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCarLocationsRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCarsRequest;
import cn.meyoung.wlfz.webapi.dto.request.ModifyCarRequest;
import cn.meyoung.wlfz.webapi.dto.response.CarResponse;
import cn.meyoung.wlfz.webapi.dto.response.CarStatusResponse;
import cn.meyoung.wlfz.webapi.dto.response.LocationResponse;
import cn.meyoung.wlfz.webapi.entity.Car;
import cn.meyoung.wlfz.webapi.entity.Location;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.mefp.CarStatusSyncApi;
import cn.meyoung.wlfz.webapi.service.CarService;
import cn.meyoung.wlfz.webapi.service.UserService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 车辆服务接口
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@RestController
@RequestMapping(value = "/cars")
public class CarController extends BaseController {

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarStatusSyncApi carStatusSyncApi;

    /**
     * 获取车辆信息
     *
     * @param id 车辆id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getCar(@PathVariable Integer id) {

        Car car = carService.find(id);
        if (null == car) {
            return ApiResultUtils.NonCar();
        }

        CarResponse carRes = BeanMapper.map(car, CarResponse.class);

        return ApiResultUtils.Success(carRes);
    }

    /**
     * 修改车辆信息
     *
     * @param id     车辆id
     * @param req    参数
     * @param errors
     * @param token  token
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResult modifyCar(@PathVariable Integer id, @Valid ModifyCarRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Car car = carService.find(id);
        if (null == car) {
            return ApiResultUtils.NonCar();
        }

        if (!token.getUserId().equals(car.getUserId()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }
        if (req.getPlate_number() != null) {
            car.setPlateNumber(req.getPlate_number());
        }
        if (req.getLocate_number() != null) {
            car.setLocateNumber(req.getLocate_number());
        }
        if (req.getLicense() != null) {
            car.setLicense(req.getLicense());
        }
        if (req.getWidth() != null) {
            car.setWidth(req.getWidth());
        }
        if (req.getHeight() != null) {
            car.setHeight(req.getHeight());
        }
        if (req.getLength() != null) {
            car.setLength(req.getLength());
        }
        if (req.getTonnage() != null) {
            car.setTonnage(req.getTonnage());
        }
        if (req.getVolume() != null) {
            car.setVolume(req.getVolume());
        }
        if (req.getModel_1() != null) {
            car.setModel1(req.getModel_1());
        }
        if (req.getModel_2() != null) {
            car.setModel2(req.getModel_2());
        }
        if (req.getMemo() != null) {
            car.setMemo(req.getMemo());
        }

        //BeanMapper.copy(req, car);


        carService.save(car);
        return ApiResultUtils.Success();
    }


    /**
     * 添加车辆
     *
     * @param req    参数
     * @param errors 错误
     * @param token  token
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addCar(@Valid AddCarRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        if (!token.getUserId().equals(req.getUser_id())) {
            return ApiResultUtils.NoPermission();
        }

        //验证车牌是否被使用
        if (carService.existsCarByPlateNumber(req.getPlate_number())) {
            //TODO:暂时用参数错误替代
            return ApiResultUtils.ErrorParameter();
        }

        Car car = BeanMapper.map(req, Car.class);
        car.setChecked(Car.IS_NOT_CHECKED);
        car.setStatus((byte) 1);
        carService.save(car);

        return ApiResultUtils.Success();
    }


    /**
     * 获取车辆列表
     *
     * @param req    参数
     * @param errors 错误
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getCars(@Valid ListCarsRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Page<Car> list = carService.list(req);
        List<CarResponse> carResponses = BeanMapper.mapList(list.getContent(), CarResponse.class);

        return ApiResultUtils.Paged(carResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

    /**
     * 获取车辆状态
     *
     * @param id 车辆id
     * @return
     */
    @RequestMapping(value = "/{id}/status", method = RequestMethod.GET)
    public ApiResult getCarStatus(@PathVariable Integer id) {

        Car car = carService.find(id);
        if (null == car) {
            return ApiResultUtils.NonCar();
        }

        CarStatusResponse res = new CarStatusResponse();
        res.setStatus(car.getStatus());
        res.setDesc(Constants.Car.getProperty(car.getStatus().toString()));

        return ApiResultUtils.Success(res);
    }

    /**
     * 设置车辆状态
     *
     * @param id     车辆id
     * @param status 车辆状态
     * @param token  token
     * @return
     */
    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ApiResult modifyCarStatus(@PathVariable Integer id, Integer status, @CurrentToken UserToken token) {

        if (null == status || status < 0 || status > 3) {
            return ApiResultUtils.ErrorParameter();
        }

        Car car = carService.find(id);
        if (null == car) {
            return ApiResultUtils.NonCar();
        }

        //如果token中的id与当前用户id不一致时，返回没有权限，除非当前用户是管理员角色
        if (!token.getUserId().equals(car.getUserId()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        car.setStatus(status.byteValue());
        carService.save(car);

        //同步状态
        carStatusSyncApi.doSync(car.getLocateNumber(), status);

        return ApiResultUtils.Success();
    }

    /**
     * 设置车辆位置
     *
     * @param id        车辆id
     * @param longitude 经度
     * @param latitude  纬度
     * @param token     token
     * @return
     */
    @RequestMapping(value = "/{id}/app_locates", method = RequestMethod.POST)
    public ApiResult modifyCarLocates(@PathVariable Integer id, BigDecimal longitude, BigDecimal latitude, @CurrentToken UserToken token) {

        Car car = carService.find(id);
        if (null == car) {
            return ApiResultUtils.NonCar();
        }

        if (!token.getUserId().equals(car.getUserId())) {
            return ApiResultUtils.NoPermission();
        }

        Location location = new Location();
        Car carid=new Car();
        carid.setId(id);
        //location.setCarId(id);
        location.setCar(carid);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        location.setLocateTime(new Date());
        location.setLocateType(Location.LOCATION_TYPE_PHONE);
        carService.modifyLocation(car, location);

        return ApiResultUtils.Success();
    }


    /**
     * 获取车辆位置列表
     *
     * @param id     车辆id
     * @param req    参数
     * @param errors 错误
     * @return
     */
    @RequestMapping(value = "/{id}/app_locates", method = RequestMethod.GET)
    public ApiResult getCarLocations(@PathVariable Integer id, ListCarLocationsRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Car car = carService.find(id);
        if (null == car) {
            return ApiResultUtils.NonCar();
        }

        Page<Location> list = carService.findCarLocations(car, req);
        List<LocationResponse> locationResponses = BeanMapper.mapList(list.getContent(), LocationResponse.class);

        return ApiResultUtils.Paged(locationResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }


}
