package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.CargoDao;
import cn.meyoung.wlfz.webapi.dto.request.GetCargoCountRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCargosRequest;
import cn.meyoung.wlfz.webapi.entity.Cargo;
import cn.meyoung.wlfz.webapi.entity.QCargo;
import cn.meyoung.wlfz.webapi.service.CargoService;
import cn.meyoung.wlfz.webapi.utils.GeoUtils;
import com.google.common.collect.Lists;
import com.mysema.query.BooleanBuilder;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * 货物信息服务实现
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
@Service
public class CargoServiceImpl implements CargoService {

    @Autowired
    private CargoDao cargoDao;

    @Override
    public Cargo find(Integer id) {
        return cargoDao.findOne(id);
    }

    @Override
    public void save(Cargo cargo) {
        cargoDao.save(cargo);
    }

    @Override
    public Page<Cargo> list(ListCargosRequest request) {

        BooleanBuilder query = new BooleanBuilder();
        QCargo cargo = QCargo.cargo;

        switch (request.getStatus()) {
            case 0:
                query.and(cargo.expiredTime.lt(new Date()));
                break;
            case 1:
                query.and(cargo.expiredTime.goe(new Date()));
                break;
            case 2:
                break;
            default:
                query.and(cargo.expiredTime.goe(new Date()));
                break;
        }

        //用户id
        if (null != request.getUser_id()) {
            query.and(cargo.user.id.eq(request.getUser_id()));
        }
        //交易状态
        if (null != request.getTrade_status()) {
            query.and(cargo.trade_status.eq(request.getTrade_status()));
        }

        //发布时间
        if (null != request.getPublishTime()) {
            query.and(cargo.publishTime.goe(request.getPublishTime()));
        }

        //起始地
        if (StringUtils.isNotBlank(request.getOrigin_code())) {
            query.and(cargo.originCode.eq(request.getOrigin_code()));
        }

        //目的地
        if (StringUtils.isNotBlank(request.getDestination_code())) {
            String[] destinations = request.getDestination_code().split(",");
            query.and(cargo.destinationCode.in(destinations));
        }
        //tips
        if (StringUtils.isNotBlank(request.getTips())) {
            query.and(cargo.tips.like('%'+request.getTips()+'%'));
        }
        //最小货重
        if (null != request.getWeight_from()) {
            query.and(cargo.weight.goe(request.getWeight_from()));
        }
        //最大货重
        if (null != request.getWeight_to()) {
            query.and(cargo.weight.loe(request.getWeight_to()));
        }

        //模糊搜索
        if (StringUtils.isNotBlank(request.getMemo())) {
            String[] arr = request.getMemo().split("\\s");
            BooleanBuilder kw_query = new BooleanBuilder();

            for (Integer i = 0; i < arr.length; i++) {
                kw_query.or(cargo.memo.like('%' + arr[i] + '%'));
            }
            query.andAnyOf(kw_query);

            //query.or(cargo.memo.like('%'+request.getMemo()+'%'));
        }

        //地图区域，东北角和西南角
        if (null != request.getNe_lat() && null != request.getNe_lng()
                && null != request.getSw_lat() && null != request.getSw_lng()
                ) {
            query.and(cargo.latitude.goe(request.getSw_lat()));
            query.and(cargo.latitude.loe(request.getNe_lat()));
            query.and(cargo.longitude.goe(request.getSw_lng()));
            query.and(cargo.longitude.loe(request.getNe_lng()));
        }

        //是否传入了经纬度,传入经纬度需要计算和该点的距离
        if (null != request.getLatitude() && null != request.getLongitude()) {

            if (ListCargosRequest.ORDER_BY_DISTANCE.equalsIgnoreCase(request.getOrderby())) {
                //距离排序

                //先取出全部数据，计算距离后分页
                List<Cargo> cargos = Lists.newArrayList(cargoDao.findAll(query));
                calculateDistance(cargos, request.getLongitude().doubleValue(), request.getLatitude().doubleValue());

                Comparator cargoCmp = ComparableComparator.getInstance();
                cargoCmp = ComparatorUtils.nullHighComparator(cargoCmp);
                Collections.sort(cargos, new BeanComparator("distance", cargoCmp));

                Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size());

                int toIndex = pageRequest.getOffset() + pageRequest.getPageSize();
                if (toIndex > cargos.size()) {
                    toIndex = cargos.size();
                }

                List<Cargo> subList = cargos.subList(pageRequest.getOffset(), toIndex);
                return new PageImpl<>(subList, pageRequest, cargos.size());
            }
        }

        Sort sort = new Sort(Sort.Direction.DESC, "publishTime");
        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);
        Page<Cargo> cargoPage = cargoDao.findAll(query, pageRequest);

        if (null != request.getLatitude() && null != request.getLongitude()) {
            calculateDistance(cargoPage.getContent(), request.getLongitude().doubleValue(), request.getLatitude().doubleValue());
        }

        return cargoPage;

    }

    /**
     * 计算距离
     *
     * @param list
     * @param longitude
     * @param latitude
     */
    private void calculateDistance(List<Cargo> list, double longitude, double latitude) {
        for (Cargo cargo : list) {
            if (null == cargo.getLatitude() || null == cargo.getLongitude()) {
                cargo.setDistance(null);
                continue;
            }
            double distance = GeoUtils.GetDistance(longitude, latitude, cargo.getLongitude().doubleValue(), cargo.getLatitude().doubleValue());
            cargo.setDistance(distance);
        }
    }

    @Override
    public long getCount(GetCargoCountRequest req) {

        BooleanBuilder query = new BooleanBuilder();
        QCargo cargo = QCargo.cargo;
        Date publishTime = req.getPublish_time();

        query.and(cargo.publishTime.gt(publishTime));
        if (req.getStatus()==null){
            req.setStatus(1);
        }

        if(req.getStatus()==0){
            //显示过期
            query.and(cargo.expiredTime.lt(new Date()));
        }else if(req.getStatus()==2){
            // 显示所有
        }else{
            //显示有效的
            query.and(cargo.expiredTime.goe(new Date()));
        }

        return cargoDao.count(query);
    }
}
