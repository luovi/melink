package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddDriverRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListDriverRequest;
import cn.meyoung.wlfz.webapi.dto.response.DriverResponse;
import cn.meyoung.wlfz.webapi.entity.Driver;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.service.DriverService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by goushicai on 14-12-8.
 */
@RestController
@RequestMapping(value = "/drivers")
public class DriverController extends BaseController {
    @Autowired
    DriverService driverService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getDriver(@PathVariable Integer id) {
        Driver driver = driverService.find(id);
        DriverResponse res = BeanMapper.map(driver, DriverResponse.class);
        return ApiResultUtils.Success(res);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addDriver(@Valid AddDriverRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        //当前用户不是管理员角色
        if (token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }
        Driver driver = BeanMapper.map(req, Driver.class);
        driverService.save(driver);
        return ApiResultUtils.Success();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getDriverList(@Valid ListDriverRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Page<Driver> list = driverService.list(req);
        List<DriverResponse> driverResponses = BeanMapper.mapList(list.getContent(), DriverResponse.class);
        return ApiResultUtils.Paged(driverResponses, list.getNumber(), list.getSize(), list.getTotalElements());

    }
}
