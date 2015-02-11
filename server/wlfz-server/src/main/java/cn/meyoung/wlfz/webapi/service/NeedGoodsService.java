package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListNeedGoodsRequest;
import cn.meyoung.wlfz.webapi.entity.NeedGoods;
import org.springframework.data.domain.Page;

/**
 * Created by goushicai on 14-12-15.
 */
public interface NeedGoodsService {
    NeedGoods find(Integer id);

    void save(NeedGoods needGoods);

    Page<NeedGoods> list(ListNeedGoodsRequest req);

    /**
     * 删除要货记录
     * @param id
     */
    void delete(Integer id);

}
