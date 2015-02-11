package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dao.TradeDao;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddTradeRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListTradeRequest;
import cn.meyoung.wlfz.webapi.dto.response.TradeResponse;
import cn.meyoung.wlfz.webapi.dto.response.UserTradeResponse;
import cn.meyoung.wlfz.webapi.entity.Car;
import cn.meyoung.wlfz.webapi.entity.Cargo;
import cn.meyoung.wlfz.webapi.entity.QTrade;
import cn.meyoung.wlfz.webapi.entity.Trade;
import cn.meyoung.wlfz.webapi.service.CarService;
import cn.meyoung.wlfz.webapi.service.CargoService;
import cn.meyoung.wlfz.webapi.service.TradeService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import com.google.common.collect.Lists;
import com.mysema.query.BooleanBuilder;
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
 * Created by goushicai on 14-11-5.
 */
@RestController
@RequestMapping(value = "/trades")
public class TradeController extends BaseController {
    @Autowired
    private TradeService tradeService;

    @Autowired
    private CargoService cargoService;

    @Autowired
    private CarService carService;

    @Autowired
    private TradeDao tradeDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getTrade(@PathVariable Integer id) {

        Trade trade = tradeService.find(id);
        if (null == trade) {
            return ApiResultUtils.NonTrade();
        }


        Cargo cargo = cargoService.find(trade.getCargoinfo().getId());
        if (null != cargo) {
            trade.setCargoinfo(cargo);
        }

        Car car = carService.find(trade.getCarinfo().getId());
        if (null != car) {
            trade.setCarinfo(car);
        }

        TradeResponse res = BeanMapper.map(trade, TradeResponse.class);

        return ApiResultUtils.Success(res);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getTradeList(@Valid ListTradeRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Page<Trade> list = tradeService.list(req);
        List<TradeResponse> tradeResponses = BeanMapper.mapList(list.getContent(), TradeResponse.class);
        return ApiResultUtils.Paged(tradeResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

    //根据用户id获取相关的交易记录
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ApiResult getTradeListByCar_user_id(@Valid ListTradeRequest req, Errors errors) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        if (req.getCargo_user_id() == null && req.getCar_user_id() == null) {
            return ApiResultUtils.Error("999", "没有指定货主或者车主的用户id");
        }

        if (req.getCargo_user_id() != null && req.getCargo_user_id() <= 0) {
            return ApiResultUtils.Error("999", "货主的用户id参数有错误");
        }

        if (req.getCar_user_id() != null && req.getCar_user_id() <= 0) {
            return ApiResultUtils.Error("999", "车主的用户id参数有错误");
        }

        if (req.getType() == null) {
            req.setType(1);
        }

        Page<Trade> list = tradeService.findListByUserID(req);
        List<UserTradeResponse> tradeResponses = BeanMapper.mapList(list.getContent(), UserTradeResponse.class);
        return ApiResultUtils.Paged(tradeResponses, list.getNumber(), list.getSize(), list.getTotalElements());

    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addTrade(@Valid AddTradeRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Trade trade = BeanMapper.map(req, Trade.class);

        if (trade.getTrade_time() == null) {
            trade.setTrade_time(new Date());
        }
        if (trade.getType() == null) {
            trade.setType(1);
        }
        //新加的交易状态为10
        trade.setStatus(10);
        Cargo cargo = cargoService.find(req.getCargo_id());
        if (cargo.getTrade_status() >= 10) {
            return ApiResultUtils.Error("999", "该货源已被接单。");
        }
        trade.setCargoinfo(cargo);
        trade.setCargo_user_id(cargo.getUser().getId());
        Car car = carService.find(req.getCar_id());
        if (cargo == null) {
            return ApiResultUtils.Error("999", "车辆信息错误。");
        }
        trade.setCarinfo(car);
        trade.setCar_user_id(car.getUserId());
        tradeService.save(trade);

        //同步修改货货物状态
        cargo.setTrade_status(trade.getStatus());
        cargoService.save(cargo);

        return ApiResultUtils.Success();
    }

    @RequestMapping(value = "/{id}/status", method = RequestMethod.POST)
    public ApiResult setTradeStatus(@PathVariable Integer id, Integer status, @CurrentToken UserToken token) {

        Trade trade = tradeService.find(id);

        if (trade == null) {
            return ApiResultUtils.NonTrade();
        }

        Trade newTrade = new Trade();
        Cargo cargo = trade.getCargoinfo();

        newTrade.setCarinfo(trade.getCarinfo());
        newTrade.setCargoinfo(cargo);
        newTrade.setType(trade.getType());
        newTrade.setCargo_user_id(trade.getCargo_user_id());
        newTrade.setCar_user_id(trade.getCar_user_id());
        newTrade.setTrade_time(new Date());
        newTrade.setStatus(status);

        tradeService.save(newTrade);

        changeCargoStatus(newTrade);

        //删除其它抢单车辆的交易信息
        if (status == 20) {
            BooleanBuilder query = new BooleanBuilder();
            QTrade qTrade = QTrade.trade;

            //query.and(qTrade.carinfo.id.eq(trade.getCarinfo().getId()));
            query.and(qTrade.cargoinfo.id.eq(trade.getCargoinfo().getId()));
            query.and(qTrade.type.eq(trade.getType()));
            query.and(qTrade.status.eq(10));
            query.and(qTrade.id.ne(trade.getId()));

            List<Trade> trades = Lists.newArrayList(tradeDao.findAll(query));
            for (Integer i = 0; i < trades.size(); i++) {
                cancelTrade(trades.get(i));
            }
        } else if (status == 1) {
            BooleanBuilder query = new BooleanBuilder();
            QTrade qTrade = QTrade.trade;

            //query.and(qTrade.carinfo.id.eq(trade.getCarinfo().getId()));
            query.and(qTrade.cargoinfo.id.eq(trade.getCargoinfo().getId()));
            query.and(qTrade.type.eq(trade.getType()));
            query.and(qTrade.status.gt(0)); //所有未取消的交易记录
            query.and(qTrade.id.ne(trade.getId()));

            List<Trade> trades = Lists.newArrayList(tradeDao.findAll(query));
            for (Integer i = 0; i < trades.size(); i++) {
                cancelTrade(trades.get(i));
            }
        }

        return ApiResultUtils.Success();
    }

    //取消交易
    private void cancelTrade(Trade trade) {
        trade.setStatus(0);
        tradeService.save(trade);

        changeCargoStatus(trade);
    }

    //修改货物状态
    private void changeCargoStatus(Trade trade) {
        Cargo cargo = trade.getCargoinfo();

        //如果所有交易都被取消，将货物的状态置为未接单状态
        if (trade.getStatus() == 0) {
            BooleanBuilder query = new BooleanBuilder();
            QTrade qTrade = QTrade.trade;
            query.and(qTrade.cargoinfo.id.eq(trade.getCargoinfo().getId()));
            query.and(qTrade.status.gt(0));

            List<Trade> trades = Lists.newArrayList(tradeDao.findAll(query));
            if (trades.size() == 0) {
                cargo.setTrade_status(1);
                cargoService.save(cargo);
            }
        } else {
            cargo.setTrade_status(trade.getStatus());
            cargoService.save(cargo);
        }
    }

}
