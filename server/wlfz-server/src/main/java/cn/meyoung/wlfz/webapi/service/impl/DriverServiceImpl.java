package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.DriverDao;
import cn.meyoung.wlfz.webapi.dto.request.ListDriverRequest;
import cn.meyoung.wlfz.webapi.entity.Driver;
import cn.meyoung.wlfz.webapi.entity.QDriver;
import cn.meyoung.wlfz.webapi.service.DriverService;
import com.mysema.query.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by goushicai on 14-12-8.
 */
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverDao driverDao;

    @Override
    public Driver find(Integer id) {
        return driverDao.findOne(id);
    }

    @Override
    public void save(Driver driver) {
        driverDao.save(driver);
    }

    @Override
    public Page<Driver> findByUserId(Integer userId) {
        return null;
    }

    @Override
    public Page<Driver> findByPlateNumber(ListDriverRequest request) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);

        return driverDao.findByPlateNumber(request.getPlate_number(), pageRequest);
    }

    @Override
    public Page<Driver> list(ListDriverRequest req) {
        BooleanBuilder query = new BooleanBuilder();
        QDriver driver = QDriver.driver;

        if (StringUtils.isNotBlank(req.getPlate_number())) {
            query.and(driver.plate_number.like('%' + req.getPlate_number() + '%'));
        }
        if (StringUtils.isNotBlank(req.getContact_number())) {
            query.and(driver.contact_number.like('%' + req.getContact_number() + '%'));
        }
        if (StringUtils.isNotBlank(req.getName())) {
            query.and(driver.name.like('%' + req.getName() + '%'));
        }
        if (StringUtils.isNotBlank(req.getId_number())) {
            query.and(driver.id_number.like('%' + req.getId_number() + '%'));
        }
        if (StringUtils.isNotBlank(req.getLicense())) {
            query.and(driver.license.like('%' + req.getLicense() + '%'));
        }
        if (req.getChecked() != null) {
            query.and(driver.checked.eq(req.getChecked()));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(req.getPage_no() - 1, req.getPage_size(), sort);

        return driverDao.findAll(query, pageRequest);
    }
}
