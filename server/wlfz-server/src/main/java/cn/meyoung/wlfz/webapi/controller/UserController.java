package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.Constants;
import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.ListUsersRequest;
import cn.meyoung.wlfz.webapi.dto.request.LoginRequest;
import cn.meyoung.wlfz.webapi.dto.request.ModifyUserRequest;
import cn.meyoung.wlfz.webapi.dto.request.RegUserRequest;
import cn.meyoung.wlfz.webapi.dto.response.LoginResponse;
import cn.meyoung.wlfz.webapi.dto.response.UserResponse;
import cn.meyoung.wlfz.webapi.entity.Car;
import cn.meyoung.wlfz.webapi.entity.Setting;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.mefp.MeFPApi;
import cn.meyoung.wlfz.webapi.service.CarService;
import cn.meyoung.wlfz.webapi.service.SettingService;
import cn.meyoung.wlfz.webapi.service.UserService;
import cn.meyoung.wlfz.webapi.service.UserTokenService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import cn.meyoung.wlfz.webapi.utils.RequestUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private MeFPApi meFPApi;

    @Autowired
    private UserTokenService userTokenService;

    /**
     * 用户登录接口
     *
     * @param loginReq 登录参数
     * @param errors   错误信息
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResult login(@Valid LoginRequest loginReq, Errors errors, HttpServletRequest request) {
        //参数验证
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        User user = userService.findByUserNameOrContactNumber(loginReq.getLogin_name());
        if (null == user) {
            return ApiResultUtils.NonUser();
        }

        //状态验证
        if (!user.getStatus().equals(User.STATUS_ENABLE)) {
            return ApiResultUtils.Error("612");
        }

        //密码验证
        if (!DigestUtils.md5Hex(loginReq.getPassword()).equals(user.getPassword())) {
            return ApiResultUtils.Error("612");
        }

        /**

         //提交到mefp进行验证
         boolean isVerified = meFPApi.userVerify(loginReq.getLogin_name(), loginReq.getPassword());

         if (!isVerified) {
         return ApiResultUtils.Error("612");
         }
         */


        //更新登录信息
        user.setLoginTime(new Date());
        user.setLoginIP(RequestUtils.getUserIPAddress(request));

        userService.save(user);

        //组装客户端配置信息
        List<Setting> settingList = settingService.findListByClient(loginReq.getClient());

        Map<String, String> settings = new HashMap<>();
        for (Setting setting : settingList) {
            if (!settings.containsKey(setting.getKey())) {
                settings.put(setting.getKey(), setting.getValue());
            }
        }

        //组装车辆信息
        List<Car> carList = carService.findByUserId(user.getId());
        List<LoginResponse.Car> cars = BeanMapper.mapList(carList, LoginResponse.Car.class);

        String token = userTokenService.create(user);

        LoginResponse loginRes = new LoginResponse();
        loginRes.setUserId(user.getId());
        loginRes.setCode(user.getCode());
        loginRes.setIsAdmin(user.getIsAdmin());
        loginRes.setAddress(user.getAddress());
        loginRes.setCompany(user.getCompany());
        loginRes.setContactName(user.getContactName());
        loginRes.setContactNumber(user.getContactNumber());
        loginRes.setUserName(user.getUserName());

        loginRes.setSettings(settings);
        loginRes.setCars(cars);
        loginRes.setToken(token);
        loginRes.setClient(loginReq.getClient());
        return ApiResultUtils.Success(loginRes);
    }

    /**
     * 获取用户信息
     *
     * @param id 用户id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getUser(@PathVariable Integer id) {

        User user = userService.find(id);
        if (null == user) {
            return ApiResultUtils.NonUser();
        }

        UserResponse userRes = BeanMapper.map(user, UserResponse.class);

        return ApiResultUtils.Success(userRes);
    }

    /**
     * 修改用户信息
     *
     * @param id     用户id
     * @param req    参数
     * @param errors 错误信息
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ApiResult modifyUser(@PathVariable Integer id, @Valid ModifyUserRequest req, Errors errors, @CurrentToken UserToken token) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        User user = userService.find(id);
        if (null == user) {
            return ApiResultUtils.NonUser();
        }

        //如果token中的id与当前用户id不一致时，返回没有权限，除非当前用户是管理员角色
        if (!token.getUserId().equals(user.getId()) && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        //TODO:密码如何与mefp同步 未完成
        if (StringUtils.isNotBlank(req.getPassword())) {
            user.setPassword(DigestUtils.md5Hex(req.getPassword()));
        }

        if(StringUtils.isNotBlank(req.getAddress())){
            user.setAddress(req.getAddress());
        }
        if(StringUtils.isNotBlank(req.getCompany())){
            user.setCompany(req.getCompany());
        }
        if(StringUtils.isNotBlank(req.getContact_name())){
            user.setContactName(req.getContact_name());
        }
        if(StringUtils.isNotBlank(req.getMemo())){
            user.setMemo(req.getMemo());
        }

        userService.save(user);
        return ApiResultUtils.Success();
    }


    /**
     * 发送验证码
     *
     * @param contactNumber 联系手机
     * @return
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public ApiResult sendCaptcha(@RequestParam("register_contact_number") String contactNumber) {


        if (StringUtils.isBlank(contactNumber)) {
            return ApiResultUtils.ErrorParameter();
        }

        //验证手机是否已经被使用
        if (userService.existsByContactNumber(contactNumber)) {
            return ApiResultUtils.Error("616");
        }

        //调用mefp发送验证信息
        String resultCode = meFPApi.sendCaptcha(contactNumber);
        if (resultCode.equals("1")) {
            return ApiResultUtils.Success();
        } else {
            return ApiResultUtils.Error(resultCode);
        }

    }

    /**
     * 用户注册
     *
     * @param req    注册参数
     * @param errors 错误信息
     * @return
     */
    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public ApiResult regUser(@Valid RegUserRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        //验证用户名和手机号码是否已经被使用

        if (userService.existsByUserName(req.getUsername()) || userService.existsByContactNumber(req.getContact_number())) {

            return ApiResultUtils.Error("616");
        }

        /*
        暂时取消调用mefp注册
        String resultCode = meFPApi.userRegister(req.getUsername(), req.getPassword(), req.getCompany(), req.getContact_number(), req.getCaptcha());

        if (!resultCode.equals("1")) {
            return ApiResultUtils.Error(resultCode);
        }
        */

        User user = BeanMapper.map(req, User.class);

        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        user.setRegisterTime(new Date());
        user.setStatus(User.STATUS_ENABLE);
        user.setChecked(User.IS_NOT_CHECKED);
        user.setIsAdmin(User.IS_NOT_ADMIN);
        String userCodePrefix = settingService.findValueByClientAndKey(Constants.CLIENT_SYSTEM, Constants.USER_CODE_PREFIX_KEY, String.class);

        user.setCode(userCodePrefix + (userService.findLastUserId() + 1));
        userService.save(user);

        return ApiResultUtils.Success();
    }

    /**
     * 获取用户列表
     *
     * @param req    参数
     * @param errors 错误信息
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getUsers(@Valid ListUsersRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        Page<User> list = userService.list(req);
        List<UserResponse> userResponses = BeanMapper.mapList(list.getContent(), UserResponse.class);

        return ApiResultUtils.Paged(userResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }

    /**
     * 用户注销
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ApiResult logout() {
        return ApiResultUtils.Success();
    }
}
