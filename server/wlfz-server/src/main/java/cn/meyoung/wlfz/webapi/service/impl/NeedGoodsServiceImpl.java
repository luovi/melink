package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.NeedGoodsDao;
import cn.meyoung.wlfz.webapi.dto.request.ListNeedGoodsRequest;
import cn.meyoung.wlfz.webapi.entity.NeedGoods;
import cn.meyoung.wlfz.webapi.entity.QNeedGoods;
import cn.meyoung.wlfz.webapi.service.NeedGoodsService;
import com.mysema.query.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by goushicai on 14-12-16.
 */
@Service
public class NeedGoodsServiceImpl implements NeedGoodsService {
    @Autowired
    private NeedGoodsDao needGoodsDao;

    @Override
    public void save(NeedGoods needGoods) {
        needGoodsDao.save(needGoods);
    }

    ;

    @Override
    public NeedGoods find(Integer id) {
        return needGoodsDao.findOne(id);
    }

    @Override
    public Page<NeedGoods> list(ListNeedGoodsRequest request) {
        BooleanBuilder query = new BooleanBuilder();
        QNeedGoods needGoods = QNeedGoods.needGoods;

        if (null != request.getUser_id()) {
            query.and(needGoods.user.id.eq(request.getUser_id()));
        }

        if (null != request.getCar_id()) {
            query.and(needGoods.car.id.eq(request.getCar_id()));
        }
        // request.getStatus = [0,1,2] 0为草稿，1为已发布，2为全部，此处理在controller中实现
        if (null != request.getStatus()) {
            if (request.getStatus() == 0 || request.getStatus() == 1) {
                query.and(needGoods.status.eq(request.getStatus()));
            } else if (request.getStatus() == 2) {
                //status=2代表取全部状态的数据
            } else {//默认为1
                query.and(needGoods.status.eq(1));
            }

        } else {//默认为1
            query.and(needGoods.status.eq(1));
        }

        if (null != request.getOrigin()) {
            query.and(needGoods.origin.like('%' + request.getOrigin() + '%'));
        }
        if (null != request.getDestination()) {
            query.and(needGoods.destination.like('%' + request.getDestination() + '%'));
        }
        if (null != request.getMemo()) {
            query.and(needGoods.memo.like('%' + request.getMemo() + '%'));
        }

        if (null != request.getBegin_time()) {
            query.and(needGoods.begin_time.goe(request.getBegin_time()));
        }

        if (null != request.getEnd_time()) {
            query.and(needGoods.end_time.goe(request.getEnd_time()));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);

        Page<NeedGoods> needGoodsPage = needGoodsDao.findAll(query, pageRequest);

        return needGoodsPage;
    }

    @Override
    public void delete(Integer id)
    {
        needGoodsDao.delete(id);
    }
}
