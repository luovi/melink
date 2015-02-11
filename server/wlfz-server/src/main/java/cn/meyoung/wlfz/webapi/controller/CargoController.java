package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.Constants;
import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddCargoRequest;
import cn.meyoung.wlfz.webapi.dto.request.GetCargoCountRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCargosRequest;
import cn.meyoung.wlfz.webapi.dto.request.ModifyCargoRequest;
import cn.meyoung.wlfz.webapi.dto.response.CargoResponse;
import cn.meyoung.wlfz.webapi.dto.response.GetCargoCountResponse;
import cn.meyoung.wlfz.webapi.entity.Cargo;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.service.CargoService;
import cn.meyoung.wlfz.webapi.service.SettingService;
import cn.meyoung.wlfz.webapi.service.UserService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 货物服务接口
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@RestController
@RequestMapping("/cargos")
public class CargoController extends BaseController {

    @Autowired
    private CargoService cargoService;

    @Autowired
    private UserService userService;

    @Autowired
    private SettingService settingService;

    /**
     * 获取货物列表
     *
     * @param req    参数
     * @param errors
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getCargoList(@Valid ListCargosRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Page<Cargo> list = cargoService.list(req);
        List<CargoResponse> cargoResponses = BeanMapper.mapList(list.getContent(), CargoResponse.class);


        return ApiResultUtils.Paged(cargoResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }


    /**
     * 获取货物信息
     *
     * @param id 货物id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getCargo(@PathVariable Integer id) {
        Cargo cargo = cargoService.find(id);
        if (null == cargo) {
            return ApiResultUtils.NonCargo();
        }

        CargoResponse res = BeanMapper.map(cargo, CargoResponse.class);
        return ApiResultUtils.Success(res);
    }


    /**
     * 添加货物信息
     *
     * @param req    参数
     * @param errors 错误
     * @param token  token
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addCargo(@Valid AddCargoRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }
        //如果token中的id与当前用户id不一致时，返回没有权限，除非当前用户是管理员角色
        if (!token.getUserId().equals(req.getUser_id()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        User user = userService.find(req.getUser_id());
        if (null == user) {
            return ApiResultUtils.NonUser();
        }

        Cargo cargo = BeanMapper.map(req, Cargo.class);
        cargo.setUser(user);

        cargo.setPublishTime(new Date());
        cargo.setTrade_status(1);

        if (null == cargo.getExpiredTime()) {
            Integer cargoExpiredMinutes = settingService.findValueByClientAndKey(Constants.CLIENT_SYSTEM, Constants.CARGO_EXPIRED_TIME_KEY, Integer.class);

            if (null == cargoExpiredMinutes) {
                cargoExpiredMinutes = 120;
                //return ApiResultUtils.Error("999", "货物过期时间参数配置错误");
            }

            Date expiredTime = DateUtils.addMinutes(new Date(), cargoExpiredMinutes);
            cargo.setExpiredTime(expiredTime);
        }

        cargoService.save(cargo);
        return ApiResultUtils.Success();
    }

    /**
     * 修改货物信息
     *
     * @param id     货物id
     * @param req    参数
     * @param errors 错误
     * @param token  token
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResult modifyCargo(@PathVariable Integer id, @Valid ModifyCargoRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Cargo cargo = cargoService.find(id);
        if (null == cargo) {
            return ApiResultUtils.NonCargo();
        }
        //如果token中的id与当前用户id不一致时，返回没有权限，除非当前用户是管理员角色
        if (!token.getUserId().equals(cargo.getUser().getId()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }
        if (req.getExpired_time() != null && req.getExpired_time().before(cargo.getPublishTime())) {
            return ApiResultUtils.Error("999", "过期时间不能早于发布时间");
        }

        if (req.getOrigin() != null) {
            cargo.setOrigin(req.getOrigin());
        }
        if (req.getDestination() != null) {
            cargo.setDestination(req.getDestination());
        }

        if (req.getOrigin_code() != null) {
            cargo.setOriginCode(req.getOrigin_code());
        }
        if (req.getDestination_code() != null) {
            cargo.setDestinationCode(req.getDestination_code());
        }
        if (req.getPublish_time() != null) {
            cargo.setPublishTime(req.getPublish_time());
        }
        if (req.getExpired_time() != null) {
            cargo.setExpiredTime(req.getExpired_time());
        }
        if (req.getCarriage() != null) {
            cargo.setCarriage(req.getCarriage());
        }
        if (req.getContact_name() != null) {
            cargo.setContact_name(req.getContact_name());
        }
        if (req.getContact_number() != null) {
            cargo.setContact_number(req.getContact_number());
        }

        if (req.getLatitude() != null) {
            cargo.setLatitude(req.getLatitude());
        }
        if (req.getLongitude() != null) {
            cargo.setLongitude(req.getLongitude());
        }
        if (req.getTrade_status() != null) {
            cargo.setTrade_status(req.getTrade_status());
        }

        if (req.getType() != null) {
            cargo.setType(req.getType());
        }
        if (req.getVolume() != null) {
            cargo.setVolume(req.getVolume());
        }
        if (req.getWeight() != null) {
            cargo.setWeight(req.getWeight());
        }

        if (req.getTips() != null) {
            cargo.setTips(req.getTips());
        }
        if (req.getMemo() != null) {
            cargo.setMemo(req.getMemo());
        }

        //BeanMapper.copy(req, cargo);

        cargoService.save(cargo);

        return ApiResultUtils.Success();
    }

    /**
     * 获取指定发布时间之后的货物数量
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ApiResult getCargoCount(@Valid GetCargoCountRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }
        //如果token中的id与当前用户id不一致时，返回没有权限，除非当前用户是管理员角色
        if (token.getUserId() == null) {
            return ApiResultUtils.NoPermission();
        }

        long count = cargoService.getCount(req);
        GetCargoCountResponse res = new GetCargoCountResponse();
        res.setCount(count);
        res.setCur_time(new Date());
        return ApiResultUtils.Success(res);
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
    public ApiResult modifyCarStatus(@PathVariable Integer id, Integer status, @CurrentToken UserToken token) {

        if (null == status || status < 0) {
            return ApiResultUtils.ErrorParameter();
        }
        //此功能为管理员功能，非管理员不能操作
        if (token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        Cargo cargo = cargoService.find(id);
        if (null == cargo) {
            return ApiResultUtils.NonCargo();
        }

        cargo.setTrade_status(status);
        cargoService.save(cargo);

        return ApiResultUtils.Success();
    }

}
