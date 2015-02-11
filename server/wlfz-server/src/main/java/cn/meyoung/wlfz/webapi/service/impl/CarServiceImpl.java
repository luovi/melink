package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.CarDao;
import cn.meyoung.wlfz.webapi.dao.LocationDao;
import cn.meyoung.wlfz.webapi.dto.request.ListCarLocationsRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCarsRequest;
import cn.meyoung.wlfz.webapi.entity.Car;
import cn.meyoung.wlfz.webapi.entity.Location;
import cn.meyoung.wlfz.webapi.entity.QCar;
import cn.meyoung.wlfz.webapi.entity.QLocation;
import cn.meyoung.wlfz.webapi.service.CarService;
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
import java.util.List;

/**
 * 车辆信息服务实现
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarDao carDao;

    @Autowired
    private LocationDao locationDao;

    @Override
    public Car find(Integer id) {
        return carDao.findOne(id);
    }

    @Override
    public List<Car> findByUserId(Integer userId) {
        return carDao.findListByUserId(userId);
    }

    @Override
    public void save(Car car) {
        carDao.save(car);
    }

    @Override
    public boolean existsCarByPlateNumber(String plateNumber) {
        long count = carDao.count(QCar.car.plateNumber.eq(plateNumber));
        return count > 0;
    }

    @Override
    public Page<Car> list(ListCarsRequest request) {
        QCar car = QCar.car;
        BooleanBuilder query = new BooleanBuilder();

        if (null != request.getUser_id()) {
            query.and(car.userId.eq(request.getUser_id()));
        }

        if (StringUtils.isNotBlank(request.getOriginal())) {
            query.and(car.original.eq(request.getOriginal()));
        }

        if (StringUtils.isNotBlank(request.getOriginal_code())) {
            query.and(car.originalCode.eq(request.getOriginal_code()));
        }

        if (StringUtils.isNotBlank(request.getModel_1())) {
            query.and(car.model1.eq(request.getModel_1()));
        }

        if (StringUtils.isNotBlank(request.getModel_2())) {
            query.and(car.model2.eq(request.getModel_2()));
        }

        if (StringUtils.isNotBlank(request.getModel_3())) {
            query.and(car.model3.eq(request.getModel_3()));
        }

        if (StringUtils.isNotBlank(request.getModel_4())) {
            query.and(car.model4.eq(request.getModel_4()));
        }

        //地图区域，东北角和西南角
        if (null != request.getNe_lat() && null != request.getNe_lng()
                && null != request.getSw_lat() && null != request.getSw_lng()
                ) {
            query.and(car.latitude.goe(request.getSw_lat()));
            query.and(car.latitude.loe(request.getNe_lat()));
            query.and(car.longitude.goe(request.getSw_lng()));
            query.and(car.longitude.loe(request.getNe_lng()));

        }
        if (null != request.getApp_dev_id()) {
            if (request.getApp_dev_id() == 1) {
                query.and(car.app_dev_id.isNotNull());
            } else {
                query.and(car.app_dev_id.isNull());
            }
        }

        if (null != request.getLength_from()) {
            query.and(car.length.goe(request.getLength_from()));
        }

        if (null != request.getLength_to()) {
            query.and(car.length.loe(request.getLength_to()));
        }

        if (null != request.getWidth_from()) {
            query.and(car.width.goe(request.getWidth_from()));
        }

        if (null != request.getWidth_to()) {
            query.and(car.width.loe(request.getWidth_to()));
        }

        if (null != request.getHeight_from()) {
            query.and(car.height.goe(request.getHeight_from()));
        }

        if (null != request.getHeight_to()) {
            query.and(car.height.loe(request.getHeight_to()));
        }

        if (null != request.getTonnage_from()) {
            query.and(car.tonnage.goe(request.getTonnage_from()));
        }

        if (null != request.getTonnage_to()) {
            query.and(car.tonnage.loe(request.getTonnage_to()));
        }

        if (StringUtils.isNotBlank(request.getKeyword())) {
            query.andAnyOf(car.plateNumber.like("%" + request.getKeyword() + "%"),
                    car.locateNumber.like("%" + request.getKeyword() + "%"),
                    car.original.like("%" + request.getKeyword() + "%"),
                    car.memo.like("%" + request.getKeyword() + "%"));
        }


        //传入经纬度
        if (null != request.getLatitude() && null != request.getLongitude()) {
            if (ListCarsRequest.ORDER_BY_DISTANCE.equalsIgnoreCase(request.getOrderby())) {
                //距离排序

                //先取出全部数据，计算距离后分页
                List<Car> cars = Lists.newArrayList(carDao.findAll(query));
                calculateDistance(cars, request.getLongitude().doubleValue(), request.getLatitude().doubleValue());


                Comparator carCmp = ComparableComparator.getInstance();
                carCmp = ComparatorUtils.nullHighComparator(carCmp);
                Collections.sort(cars, new BeanComparator("distance", carCmp));

                Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size());

                int toIndex = pageRequest.getOffset() + pageRequest.getPageSize();
                if (toIndex > cars.size()) {
                    toIndex = cars.size();
                }

                List<Car> subList = cars.subList(pageRequest.getOffset(), toIndex);
                return new PageImpl<>(subList, pageRequest, cars.size());
            }
        }


        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);
        Page<Car> carPage = carDao.findAll(query, pageRequest);

        if (null != request.getLatitude() && null != request.getLongitude()) {
            calculateDistance(carPage.getContent(), request.getLongitude().doubleValue(), request.getLatitude().doubleValue());
        }

        return carPage;
    }

    @Override
    public void modifyLocation(Car car, Location location) {
        car.setLatitude(location.getLatitude());
        car.setLongitude(location.getLongitude());
        carDao.save(car);
        locationDao.save(location);
    }

    @Override
    public Page<Location> findCarLocations(Car car, ListCarLocationsRequest request) {

        BooleanBuilder query = new BooleanBuilder();
        QLocation location = QLocation.location;

        query.and(location.car.id.eq(car.getId()));

        if (null != request.getType()) {
            query.and(location.locateType.eq(request.getType()));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "locateTime");

        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);
        return locationDao.findAll(query, pageRequest);
    }


    /**
     * 计算距离
     *
     * @param list
     * @param longitude
     * @param latitude
     */
    private void calculateDistance(List<Car> list, double longitude, double latitude) {
        for (Car car : list) {

            if (null == car.getLatitude() || null == car.getLongitude()) {

                car.setDistance(null);
                continue;
            }
            double distance = GeoUtils.GetDistance(longitude, latitude, car.getLongitude().doubleValue(), car.getLatitude().doubleValue());
            car.setDistance(distance);
        }
    }

}
