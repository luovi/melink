package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddCalllogRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCalllogRequest;
import cn.meyoung.wlfz.webapi.dto.response.CalllogResponse;
import cn.meyoung.wlfz.webapi.entity.*;
import cn.meyoung.wlfz.webapi.service.*;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
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
 * Created by goushicai on 14-11-4.
 */

@RestController
@RequestMapping(value = "/calllogs")
public class CalllogController extends BaseController {
    @Autowired
    private CalllogService calllogService;

    @Autowired
    private UserService userService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private CarService carService;

    @Autowired
    private CargoService cargoService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getCalllog(@PathVariable Integer id) {

        Calllog calllog = calllogService.find(id);
        if (null == calllog) {
            return ApiResultUtils.NonCalllog();
        }
        User fromUserInfo = userService.find(calllog.getFrom_user());
        User toUserInfo = userService.find(calllog.getTo_user());
        Car carInfo = carService.find(calllog.getCar_id());

        calllog.setFromUserInfo(fromUserInfo);
        calllog.setToUserInfo(toUserInfo);
        calllog.setCarInfo(carInfo);


        CalllogResponse res = BeanMapper.map(calllog, CalllogResponse.class);

        return ApiResultUtils.Success(res);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addCalllog(@Valid AddCalllogRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        //如果token中的id与当前用户id不一致时，返回没有权限，除非当前用户是管理员角色
        if (!token.getUserId().equals(req.getFrom_user()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        Calllog calllog = BeanMapper.map(req, Calllog.class);

        Date now = new Date();
        calllog.setEnd_time(now);

        if (req.getHolding_time() != null) {
            // 用户可以直接确认交易，目前没有其它方式区别，用通话时长为10000秒作为确认交易的通话记录。
            long t = now.getTime() - (1000 * req.getHolding_time());
            calllog.setStart_time(new Date(t));
        }

        if (req.getType() == null) {
            calllog.setType(1);
        }
        if (req.getResult() == null) {
            calllog.setResult(0);
        }
        if (req.getFrom_number() == null) {
            User user = userService.find(req.getFrom_user());
            if (null != user) {
                calllog.setFrom_number(user.getContactNumber());
            }
        }
        if (req.getTo_number() == null) {
            User user = userService.find(req.getTo_user());
            if (null != user) {
                calllog.setTo_number(user.getContactNumber());
            }
        }

        calllogService.save(calllog);
        //如果通话结果为达成交易，则添加交易记录
        if (calllog.getResult() == 1) {
            Trade trade = new Trade();
            Car car = carService.find(calllog.getCar_id());
            Cargo cargo = cargoService.find(calllog.getCargo_id());

            //修改货源交易状态
            cargo.setTrade_status(10);
            cargoService.save(cargo);

            //添加交易记录
            trade.setCarinfo(car);
            trade.setCargoinfo(cargo);
            trade.setCar_user_id(car.getUserId());
            trade.setCargo_user_id(cargo.getUser().getId());
            trade.setTrade_time(new Date());
            trade.setType(calllog.getType());
            trade.setStatus(10);

            tradeService.save(trade);
        }

        return ApiResultUtils.Success();
    }


    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getCalllogList(@Valid ListCalllogRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Page<Calllog> list = calllogService.list(req);
        List<CalllogResponse> calllogResponses = BeanMapper.mapList(list.getContent(), CalllogResponse.class);
        return ApiResultUtils.Paged(calllogResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

}
