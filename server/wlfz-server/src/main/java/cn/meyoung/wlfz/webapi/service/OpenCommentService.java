package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.ListCommentRequest;
import cn.meyoung.wlfz.webapi.entity.OpenComment;
import org.springframework.data.domain.Page;

/**
 * 评论服务
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
public interface OpenCommentService {

    /**
     * 获取评论
     *
     * @param id 评论id
     * @return 评论信息
     */
    OpenComment find(Integer id);

    /**
     * 保存评论
     *
     * @param openComment 评论
     */
    void save(OpenComment openComment);


    /**
     * 删除评论
     *
     * @param id 评论id
     */
    void delete(Integer id);


    /**
     * 删除评论
     *
     * @param openComment 评论
     */
    void delete(OpenComment openComment);


    /**
     * 评论列表
     *
     * @param request 参数
     * @return 评论列表
     */
    Page<OpenComment> list(ListCommentRequest request);
}
