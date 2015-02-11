package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListMessagesRequest;
import cn.meyoung.wlfz.webapi.entity.Message;
import org.springframework.data.domain.Page;

/**
 * message相关业务方法
 * Created by shaowei
 * on 2014/12/26
 */
public interface MessageService {
    /**
     * 保存信息
     *
     * @param message 短信或jpush推送成功的信息
     */
    void Save(Message message);

    /**
     * 获取某条message信息
     *
     * @param id message id
     * @return message
     */
    Message find(Integer id);

    /**
     * 根据条件获取信息列表
     *
     * @param request 请求的条件定义的参数
     * @return 货物列表
     */
    Page<Message> list(ListMessagesRequest request);

}
