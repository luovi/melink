package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.OpenCommentDao;
import cn.meyoung.wlfz.webapi.dto.request.ListCommentRequest;
import cn.meyoung.wlfz.webapi.entity.OpenComment;
import cn.meyoung.wlfz.webapi.entity.QOpenComment;
import cn.meyoung.wlfz.webapi.service.OpenCommentService;
import com.mysema.query.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author 林守锦
 * @version 1.0
 * @date 2014-09-25
 */
@Service
public class OpenCommentServiceImpl implements OpenCommentService {

    @Autowired
    private OpenCommentDao openCommentDao;

    @Override
    public OpenComment find(Integer id) {
        return openCommentDao.findOne(id);
    }

    @Override
    public void save(OpenComment openComment) {
        openCommentDao.save(openComment);
    }

    @Override
    public void delete(Integer id) {
        openCommentDao.delete(id);
    }

    @Override
    public void delete(OpenComment openComment) {
        openCommentDao.delete(openComment);
    }

    @Override
    public Page<OpenComment> list(ListCommentRequest request) {

        BooleanBuilder query = new BooleanBuilder();

        QOpenComment openComment = QOpenComment.openComment;
        if (null != request.getUser_id()) {
            query.and(openComment.userId.eq(request.getUser_id()));
        }

        if (null != request.getComment_userid()) {
            query.and(openComment.commentUserId.eq(request.getComment_userid()));
        }

        if (null != request.getComment_time_from()) {
            query.and(openComment.commentTime.goe(request.getComment_time_from()));
        }

        if (null != request.getComment_time_to()) {
            query.and(openComment.commentTime.loe(request.getComment_time_to()));
        }

        if (null != request.getCar_id()) {
            query.and(openComment.car_id.eq(request.getCar_id()));
        }
        if (null != request.getCargo_id()) {
            query.and(openComment.cargo_id.eq(request.getCargo_id()));
        }

        Sort sort = new Sort(Sort.Direction.DESC, "commentTime");

        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);
        return openCommentDao.findAll(query, pageRequest);
    }
}
