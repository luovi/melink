package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.request.ListLocationRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListNoticeRequest;
import cn.meyoung.wlfz.webapi.dto.response.LocationResponse;
import cn.meyoung.wlfz.webapi.dto.response.NoticeResponse;
import cn.meyoung.wlfz.webapi.entity.Location;
import cn.meyoung.wlfz.webapi.entity.Notice;
import cn.meyoung.wlfz.webapi.service.LocationService;
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
 * Created by shaowei on 2015/1/4.
 * 定位相关
 */
@RestController
@RequestMapping("/Location")
public class LocationController extends  BaseController{
    @Autowired
    private LocationService locationService;

    /**
     * 获取某条记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getLocation(@PathVariable Integer id) {
        Location location = locationService.Find(id);
        if (null == location) {
            return ApiResultUtils.NonLocation();
        }

        //调用beanmapper进行entity和Dto之间的转换，也可以手动设置get,set操作。。
        //BeanMapper自定义的类，其中关联dozerBeanMapping.xml
        LocationResponse locationResponse = BeanMapper.map(location, LocationResponse.class);

        //自定义
        return ApiResultUtils.Success(locationResponse);
    }

    /**
     * 获取列表。。
     * @param req
     * @param errors
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getLocations(@Valid ListLocationRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

     /*  也可以对输入的请求条件参数进一步判断
        if (req.getBegin_time() == null && req.getEnd_time() == null) {
            req.setEnd_time(new Date());
        }*/

        Page<Location> list = locationService.List(req);
        //BeanMapper自定义的类，其中关联dozerBeanMapping.xml
        List<LocationResponse> locationResponses = BeanMapper.mapList(list.getContent(), LocationResponse.class);

        //自定义
        return ApiResultUtils.Paged(locationResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

}
