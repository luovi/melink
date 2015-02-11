package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.CalllogDao;
import cn.meyoung.wlfz.webapi.dto.request.ListCalllogRequest;
import cn.meyoung.wlfz.webapi.entity.Calllog;
import cn.meyoung.wlfz.webapi.entity.QCalllog;
import cn.meyoung.wlfz.webapi.service.CalllogService;
import com.mysema.query.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by goushicai on 14-11-4.
 */

@Service
public class CalllogServiceImpl implements CalllogService {

    @Autowired
    private CalllogDao calllogDao;


    @Override
    public Calllog find(Integer id) {
        return calllogDao.findOne(id);
    }

    // 保存通话记录，当通话结果是已经成交时，同时添加交易记录
    @Override
    public void save(Calllog calllog) {
        calllogDao.save(calllog);


    }

    @Override
    public Page<Calllog> list(ListCalllogRequest request) {
        BooleanBuilder query = new BooleanBuilder();
        QCalllog calllog = QCalllog.calllog;

        //拨出手机号码
        if (StringUtils.isNotBlank(request.getFrom_number())) {
            query.and(calllog.from_number.eq(request.getFrom_number()));
        }
        //接收手机号码
        if (StringUtils.isNotBlank(request.getTo_number())) {
            query.and(calllog.to_number.eq(request.getTo_number()));
        }

        //货源id
        if (null != request.getCargo_id()) {
            query.and(calllog.cargo_id.eq(request.getCargo_id()));
        }

        //车辆id
        if (null != request.getCar_id()) {
            query.and(calllog.cargo_id.eq(request.getCar_id()));
        }

        //拨出用户id
        if (null != request.getFrom_user()) {
            query.and(calllog.from_user.eq(request.getFrom_user()));
        }

        //接听用户id
        if (null != request.getTo_user()) {
            query.and(calllog.to_user.eq(request.getTo_user()));
        }

        //通话类型,1:车找货，2:货找车
        if (null != request.getType()) {
            query.and(calllog.type.eq(request.getType()));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);

        Page<Calllog> calllogPage = calllogDao.findAll(query, pageRequest);

        return calllogPage;
    }
}
