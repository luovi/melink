package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddCommentRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCommentRequest;
import cn.meyoung.wlfz.webapi.dto.request.ModifyCommentRequest;
import cn.meyoung.wlfz.webapi.dto.response.CommentResponse;
import cn.meyoung.wlfz.webapi.entity.OpenComment;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.service.OpenCommentService;
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
 * 评论接口
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
@RestController
@RequestMapping("/comments")
public class CommentController extends BaseController {

    @Autowired
    private OpenCommentService openCommentService;

    /**
     * 新增评论
     *
     * @param req    评论参数
     * @param errors 错误
     * @param token  token
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addComment(@Valid AddCommentRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        if (req.getUser_id().equals(req.getComment_userid())) {
            return ApiResultUtils.Error("999", "不能给自己添加评价");
        }
        //登录用户与评价用户必须相同，除非是管理员
        if (!token.getUserId().equals(req.getComment_userid()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        OpenComment openComment = BeanMapper.map(req, OpenComment.class);
        if (null == openComment.getCommentTime()) {
            openComment.setCommentTime(new Date());
        }

        openCommentService.save(openComment);

        return ApiResultUtils.Success();
    }


    /**
     * 获取评论列表
     *
     * @param req    参数
     * @param errors 错误
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getComments(@Valid ListCommentRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Page<OpenComment> list = openCommentService.list(req);
        List<CommentResponse> commentResponses = BeanMapper.mapList(list.getContent(), CommentResponse.class);

        return ApiResultUtils.Paged(commentResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

    /**
     * 修改评论
     *
     * @param id     id
     * @param req    修改参数
     * @param errors 错误
     * @param token  token
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResult modifyComment(@PathVariable Integer id, @Valid ModifyCommentRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        OpenComment openComment = openCommentService.find(id);
        if (null == openComment) {
            //TODO:先用参数错误来替代
            return ApiResultUtils.ErrorParameter();
        }

        if (!token.getUserId().equals(openComment.getCommentUserId())) {
            return ApiResultUtils.NoPermission();
        }

        BeanMapper.copy(req, openComment);
        openCommentService.save(openComment);

        return ApiResultUtils.Success();
    }


    /**
     * 删除评论
     *
     * @param id    评论id
     * @param token token
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ApiResult deleteComment(@PathVariable Integer id, @CurrentToken UserToken token) {

        OpenComment openComment = openCommentService.find(id);
        if (null == openComment) {
            //TODO:先用参数错误来替代
            return ApiResultUtils.ErrorParameter();
        }

        if (!token.getUserId().equals(openComment.getCommentUserId())) {
            return ApiResultUtils.NoPermission();
        }

        openCommentService.delete(openComment);

        return ApiResultUtils.Success();
    }
}
