package cn.meyoung.wlfz.webapi.controller;

import antlr.StringUtils;
import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.*;
import cn.meyoung.wlfz.webapi.dto.response.MessageResponse;
import cn.meyoung.wlfz.webapi.dto.response.NoticeResponse;
import cn.meyoung.wlfz.webapi.entity.*;
import cn.meyoung.wlfz.webapi.service.MessageService;
import cn.meyoung.wlfz.webapi.service.NoticeService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by shaowei on 2015/1/4.
 */
@RestController
@RequestMapping("/notice")
public class NoticeController extends  BaseController{
    @Autowired
    private NoticeService noteiceService;

    /**
     * 添加公告
     * @param request
     * @param errors
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
       public ApiResult addNotice(@Valid AddNoticeRequest request, Errors errors) {
        //@Valid注解会对request参数格式验证
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Notice notice = BeanMapper.map(request, Notice.class);


        //添加参数，要存入数据库表字段的; 要验证默认值
        //默认当前时间。。
        if (null == request.getPublish_time()) {
            notice.setPublish_time(new Date());
        }


        noteiceService.Save(notice);

        //自定义
        return ApiResultUtils.Success();
    }

    /**
     * 删除某条公告
     * @param id
     * @param token
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiResult deleteNotice(@PathVariable Integer id, @CurrentToken UserToken token) {

        Notice notice = noteiceService.Find(id);
        if (null == notice) {
            return ApiResultUtils.ErrorParameter();
        }

        //在底部run 窗口中可以看到 打出的消息。。
        // System.out.print("abc1234567890");
        //System.out.print(token.getUserId());
        //只有自己和管理员才有权限删除。。
        if (token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        noteiceService.Delete(id);

        return ApiResultUtils.Success();
    }


    /**
     * 获取某条记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getNotice(@PathVariable Integer id) {
        Notice notice = noteiceService.Find(id);
        if (null == notice) {
            return ApiResultUtils.NonNotice();
        }

        //调用beanmapper进行entity和Dto之间的转换，也可以手动设置get,set操作。。
        //BeanMapper自定义的类，其中关联dozerBeanMapping.xml
        NoticeResponse noticeResponse = BeanMapper.map(notice, NoticeResponse.class);

        //自定义
        return ApiResultUtils.Success(noticeResponse);
    }


    /**
     * 获取列表。。
     * @param req
     * @param errors
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getNotices(@Valid ListNoticeRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

     /*  也可以对输入的请求条件参数进一步判断
        if (req.getBegin_time() == null && req.getEnd_time() == null) {
            req.setEnd_time(new Date());
        }*/

        Page<Notice> list = noteiceService.List(req);
        //BeanMapper自定义的类，其中关联dozerBeanMapping.xml
        List<NoticeResponse> noticeResponses = BeanMapper.mapList(list.getContent(), NoticeResponse.class);

        //自定义
        return ApiResultUtils.Paged(noticeResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

    /**
     * 修改公告
     * @param id
     * @param req
     * @param errors
     * @param token
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResult modifyNotice(@PathVariable Integer id, @Valid ModifyNoticeRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Notice notice = noteiceService.Find(id);
        if (null == notice) {
            return ApiResultUtils.NonNotice();
        }
        //如果token中的id与当前用户id不一致时，返回没有权限，除非当前用户是管理员角色
        if (token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(req.getContent()) &&
                org.apache.commons.lang3.StringUtils.isNotBlank(req.getContent())
                ) {
            notice.setContent(req.getContent());
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(req.getTitle()) &&
                org.apache.commons.lang3.StringUtils.isNotBlank(req.getTitle())
                ) {
            notice.setTitle(req.getTitle());
        }
        //BeanMapper.copy(req, cargo);

        noteiceService.Save(notice);

        return ApiResultUtils.Success();
    }

}
