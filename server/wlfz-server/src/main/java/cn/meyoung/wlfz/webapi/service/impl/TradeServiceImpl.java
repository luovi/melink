package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.TradeDao;
import cn.meyoung.wlfz.webapi.dto.request.ListTradeRequest;
import cn.meyoung.wlfz.webapi.entity.QTrade;
import cn.meyoung.wlfz.webapi.entity.Trade;
import cn.meyoung.wlfz.webapi.service.TradeService;
import com.mysema.query.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by goushicai on 14-11-5.
 */
@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    private TradeDao tradeDao;

    @Override
    public Trade find(Integer id) {
        return tradeDao.findOne(id);
    }

    @Override
    public void save(Trade trade) {
        tradeDao.save(trade);
    }

    @Override
    public Page<Trade> list(ListTradeRequest request) {
        BooleanBuilder query = new BooleanBuilder();
        QTrade trade = QTrade.trade;

        //货源id
        if (null != request.getCargo_id()) {
            query.and(trade.cargoinfo.id.eq(request.getCargo_id()));
        }

        //车辆id
        if (null != request.getCar_id()) {
            query.and(trade.carinfo.id.eq(request.getCar_id()));
        }

        //车方用户id
        if (null != request.getCar_user_id()) {
            query.and(trade.car_user_id.eq(request.getCar_user_id()));
        }
        //货方用户id
        if (null != request.getCargo_user_id()) {
            query.and(trade.cargo_user_id.eq(request.getCargo_user_id()));
        }

        //状态
        if (null != request.getStatus()) {
            query.and(trade.status.eq(request.getStatus()));
        } else {
            query.and(trade.status.gt(0));
        }

        //交易时间起
        if (null != request.getFrom_time()) {
            query.and(trade.trade_time.goe(request.getFrom_time()));
        }
        //交易时间止
        if (null != request.getTo_time()) {
            query.and(trade.trade_time.loe(request.getTo_time()));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);

        Page<Trade> tradePage = tradeDao.findAll(query, pageRequest);

        return tradePage;
    }

    @Override
    public Page<Trade> findListByUserID(ListTradeRequest request) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);
        Page<Trade> tradePage = null;

        /*
        Integer status = 99;
        if (request.getStatus() != null) {
            status = request.getStatus();
        }
        */
        if (request.getType() == 1) {
            tradePage = tradeDao.findByCar_user_id(request.getCar_user_id(), pageRequest);
        } else if (request.getType() == 2) {
            tradePage = tradeDao.findByCargo_user_id(request.getCargo_user_id(), pageRequest);

        }

        return tradePage;
    }
}
