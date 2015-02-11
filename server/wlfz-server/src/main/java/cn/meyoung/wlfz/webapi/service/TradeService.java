package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListTradeRequest;
import cn.meyoung.wlfz.webapi.entity.Trade;
import org.springframework.data.domain.Page;

/**
 * Created by goushicai on 14-11-5.
 */
public interface TradeService {
    Trade find(Integer id);

    void save(Trade trade);

    Page<Trade> list(ListTradeRequest request);

    Page<Trade> findListByUserID(ListTradeRequest request);
}
