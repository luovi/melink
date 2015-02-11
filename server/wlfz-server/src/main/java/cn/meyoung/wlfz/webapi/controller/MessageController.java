package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.ErrorApiResult;
import cn.meyoung.wlfz.webapi.dto.request.AddMessageRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListMessagesRequest;
import cn.meyoung.wlfz.webapi.dto.response.MessageResponse;
import cn.meyoung.wlfz.webapi.entity.Message;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.service.MessageService;
import cn.meyoung.wlfz.webapi.service.UserService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by shaowei
 * on 2014/12/29.
 * message信息相关控制器
 */
@RestController //标识成Restcontroller输出结果都是json格式(jackjson)
@RequestMapping("/message")//映射contextpath/message
public class MessageController extends BaseController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /*可以设置默认的value
    @Value("#{settings['mefp.smsApi']}")
    private String smsApi;*/

    /**
     * 记录信息
     * ApiResult自定义
     *
     * @param request 添加参数，要存入数据库表字段的
     * @param errors  错误信息
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addMessage(@Valid AddMessageRequest request, Errors errors) {
        //@Valid注解会对request参数格式验证
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        //因为关联user信息字段
       /* User user = userService.find(request.getUser_id());
        if (null == user) {
            return ApiResultUtils.NonUser();
        }*/


        Message message = BeanMapper.map(request, Message.class);
      /* 这种是实体entity中自定义的变量IS_NOT_CHECKED-----也就是通过set,get方法负值
      request参数并没有对应entity中的checked,status字段，需要自己给他负值
       car.setChecked(Car.IS_NOT_CHECKED);
        car.setStatus((byte) 1);*/
        //  message.setUser(user);


        //添加参数，要存入数据库表字段的; 要验证默认值
        //类似NeedGoodsController的add方法
        //默认状态为 1
        if (null == request.getType()) {
            message.setType(1);
        }
        //默认当前时间。。
        if (null == request.getSend_time()) {
            message.setSend_time(new Date());
        }

        //参数限定了。@min(0)，所以必须设置默认值;否则报错
        if (null == request.getCargo_id()) {
            message.setCargo_id(0);
        }



        messageService.Save(message);

        //自定义
        return ApiResultUtils.Success();
    }


    /**
     * 根据id获取某条信息
     * ApiResult自定义的
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getMessage(@PathVariable Integer id) {
        Message message = messageService.find(id);
        if (null == message) {
            return ApiResultUtils.NonMessage();
        }

        //因为需要输出connect_name ,user字段---注意:如果mapping.xml映射过了关系，这里就不用再find查询一次；
        //可以直接用 BeanMapper.map来操作；当然如果不用 BeanMapper.map,也可以手动给entity set负值，此时就需要
        //find单独查询user信息了 ;类似needgoods操作。
     /*   User user = userService.find(message.getUser().getId());
        if (null != user) {
            message.setUser(user);
        }*/

        //调用beanmapper进行entity和Dto之间的转换，也可以手动设置get,set操作。。
        //BeanMapper自定义的类，其中关联dozerBeanMapping.xml
        MessageResponse messageResponse = BeanMapper.map(message, MessageResponse.class);

        //自定义
        return ApiResultUtils.Success(messageResponse);
    }


    /**
     * 查询信息列表
     * ApiResult自定义
     *
     * @param req    参数
     * @param errors 错误
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getMessages(@Valid ListMessagesRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

     /*  也可以对输入的请求条件参数进一步判断
        if (req.getBegin_time() == null && req.getEnd_time() == null) {
            req.setEnd_time(new Date());
        }*/

        Page<Message> list = messageService.list(req);
        //BeanMapper自定义的类，其中关联dozerBeanMapping.xml
        List<MessageResponse> carResponses = BeanMapper.mapList(list.getContent(), MessageResponse.class);

        //自定义
        return ApiResultUtils.Paged(carResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

}
