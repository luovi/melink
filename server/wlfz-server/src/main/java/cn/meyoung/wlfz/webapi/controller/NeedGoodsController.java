package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddNeedGoodsRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListNeedGoodsRequest;
import cn.meyoung.wlfz.webapi.dto.request.ModifyNeedGoodsRequest;
import cn.meyoung.wlfz.webapi.dto.response.NeedGoodsResponse;
import cn.meyoung.wlfz.webapi.entity.Car;
import cn.meyoung.wlfz.webapi.entity.NeedGoods;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.service.CarService;
import cn.meyoung.wlfz.webapi.service.NeedGoodsService;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by goushicai on 14-12-16.
 */
@RestController
@RequestMapping(value = "/needgoods")
public class NeedGoodsController extends BaseController {
    @Autowired
    NeedGoodsService needGoodsService;

    @Autowired
    UserService userService;

    @Autowired
    CarService carService;

    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getNeedGoodsList(@Valid ListNeedGoodsRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        /*
        if (token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {//非管理员只能查看自己的记录
            req.setUser_id(token.getUserId());
            req.setStatus(2);
        }
        */

        //默认只是取结束时间大于当前时间的要货车辆
        if (req.getBegin_time() == null && req.getEnd_time() == null) {
            req.setEnd_time(new Date());
        }
        //默认只是取已发布的数据
        if (req.getStatus() == null) {
            req.setStatus(1);
        }

        Page<NeedGoods> list = needGoodsService.list(req);
        List<NeedGoodsResponse> needGoodsResponses = BeanMapper.mapList(list.getContent(), NeedGoodsResponse.class);
        return ApiResultUtils.Paged(needGoodsResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getNeedGoods(@PathVariable Integer id) {

        NeedGoods needGoods = needGoodsService.find(id);
        if (null == needGoods) {
            return ApiResultUtils.NonNeedGoods();
        }

        User user = userService.find(needGoods.getUser().getId());
        if (null != user) {
            needGoods.setUser(user);
        }

        Car car = carService.find(needGoods.getCar().getId());
        if (null != car) {
            needGoods.setCar(car);
        }

        NeedGoodsResponse res = BeanMapper.map(needGoods, NeedGoodsResponse.class);

        return ApiResultUtils.Success(res);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addNeedGoods(@Valid AddNeedGoodsRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        NeedGoods needGoods = new NeedGoods();

        User user = userService.find(req.getUser_id());
        if (user == null) {
            return ApiResultUtils.NonUser();
        }
        needGoods.setUser(user);

        Car car = carService.find(req.getCar_id());
        if (car == null) {
            return ApiResultUtils.NonCar();
        }
        needGoods.setCar(car);

        //默认状态为 1
        if (req.getStatus() == null) {
            needGoods.setStatus(1);
        } else {
            needGoods.setStatus(req.getStatus());
        }

        if (req.getPublish_time() == null) {
            needGoods.setPublish_time(new Date());
        } else {
            needGoods.setPublish_time(req.getPublish_time());
        }

        //过期时间，暂时没用
        if (req.getExpired_time() == null) {//过期时期默认为24小时
            Calendar cal = Calendar.getInstance();//当前时间
            cal.add(Calendar.DATE, 1);//明天
            needGoods.setExpired_time(cal.getTime());
        }

        if (req.getBegin_time() == null) {//开始时间，默认为当前时间
            needGoods.setBegin_time(new Date());
        } else {
            needGoods.setBegin_time(req.getBegin_time());
        }

        if (req.getEnd_time() == null) {//结束时间，默认为明天下午18点
            Calendar cal = Calendar.getInstance();//当前时间
            cal.add(Calendar.DATE, 1);//明天
            cal.set(Calendar.HOUR_OF_DAY, 18);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            needGoods.setEnd_time(cal.getTime());
        } else {
            needGoods.setEnd_time(req.getEnd_time());
        }

        needGoods.setOrigin(req.getOrigin());
        needGoods.setDestination(req.getDestination());
        needGoods.setMemo(req.getMemo());

        needGoodsService.save(needGoods);
        return ApiResultUtils.Success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResult getNeedGoods(@PathVariable Integer id, @Valid ModifyNeedGoodsRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }
        NeedGoods needGoods = needGoodsService.find(id);
        if (null == needGoods) {
            return ApiResultUtils.NonNeedGoods();
        }
        if (!token.getUserId().equals(needGoods.getUser().getId()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        if (req.getBegin_time() != null) {
            needGoods.setBegin_time(req.getBegin_time());
        }
        if (req.getEnd_time() != null) {
            needGoods.setEnd_time(req.getEnd_time());
        }
        if (req.getExpired_time() != null) {
            needGoods.setExpired_time(req.getExpired_time());
        }

        if (req.getStatus() != null) {
            needGoods.setStatus(req.getStatus());
        }
        if (req.getOrigin() != null) {
            needGoods.setOrigin(req.getOrigin());
        }
        if (req.getDestination() != null) {
            needGoods.setDestination(req.getDestination());
        }
        if (req.getMemo() != null) {
            needGoods.setMemo(req.getMemo());
        }
        needGoodsService.save(needGoods);

        return ApiResultUtils.Success();
    }

    /**
     * 删除要货
     *
     * @param id    要货记录id
     * @param @CurrentToken UserToken token
     * 调用的时候不用传入；因为每次调用接口都回传入token消息头，
     * 这里只是获取一下而已。。比较user_id是否有管理权权限。
     * @return
     */
    @RequestMapping(value = "/needgood/{id}", method = RequestMethod.DELETE)
    public ApiResult deleteNeedGood(@PathVariable Integer id, @CurrentToken UserToken token) {

        NeedGoods needGoods = needGoodsService.find(id);
        if (null == needGoods) {
            return ApiResultUtils.ErrorParameter();
        }

        //在底部run 窗口中可以看到 打出的消息。。
        // System.out.print("abc1234567890");
        //System.out.print(token.getUserId());
        //只有自己和管理员才有权限删除。。
        if (!token.getUserId().equals(needGoods.getUser().getId()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        needGoodsService.delete(id);

        return ApiResultUtils.Success();
    }
}
