package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.UserDao;
import cn.meyoung.wlfz.webapi.dto.request.ListUsersRequest;
import cn.meyoung.wlfz.webapi.entity.QUser;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.service.UserService;
import com.mysema.query.BooleanBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 用户信息服务实现
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/20
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User find(Integer id) {
        return userDao.findOne(id);
    }

    @Override
    public boolean exists(Integer id) {
        return userDao.exists(id);
    }

    @Override
    public boolean existsByUserName(String userName) {

        long count = userDao.count(QUser.user.userName.eq(userName));
        return count > 0;
    }

    @Override
    public boolean existsByContactNumber(String contactNumber) {

        long count = userDao.count(QUser.user.contactNumber.eq(contactNumber));
        return count > 0;
    }

    @Override
    public User findByUserName(String userName) {

        return userDao.findOne(QUser.user.userName.eq(userName));
    }

    @Override
    public User findByContactNumber(String contactNumber) {

        return userDao.findOne(QUser.user.contactNumber.eq(contactNumber));
    }

    @Override
    public User findByUserNameOrContactNumber(String loginName) {

        User user = findByContactNumber(loginName);
        if (null == user) {
            user = findByUserName(loginName);
        }

        return user;
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public Integer findLastUserId() {
        Integer lastId = userDao.findLastUserId();
        if (null == lastId) {
            lastId = 0;
        }
        return lastId;
    }

    @Override
    public Page<User> list(ListUsersRequest request) {

        QUser user = QUser.user;
        BooleanBuilder query = new BooleanBuilder();

        if (null != request.getStatus()) {
            query.and(user.status.eq(request.getStatus()));
        }

        if (null != request.getChecked()) {
            query.and(user.checked.eq(request.getChecked()));
        }

        if (StringUtils.isNotBlank(request.getUser_name())) {
            query.and(user.userName.like("%" + request.getUser_name() + "%"));
        }

        if (StringUtils.isNotBlank(request.getCompany())) {
            query.and(user.company.like("%" + request.getCompany() + "%"));
        }

        if(StringUtils.isNotBlank(request.getKeyword())){
            query.or(user.userName.like("%" + request.getKeyword() + "%"));
            query.or(user.contactNumber.like("%" + request.getKeyword() + "%"));
            query.or(user.contactName.like("%" + request.getKeyword() + "%"));
            query.or(user.code.like("%" + request.getKeyword() + "%"));
            query.or(user.company.like("%" + request.getKeyword() + "%"));
        }

        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size());
        return userDao.findAll(query, pageRequest);
    }
}
