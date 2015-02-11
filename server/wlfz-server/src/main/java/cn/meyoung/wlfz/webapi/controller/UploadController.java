package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.bind.CurrentToken;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.UserToken;
import cn.meyoung.wlfz.webapi.dto.request.AddUploadfileRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListUploadfileRequest;
import cn.meyoung.wlfz.webapi.dto.response.UploadfileResponse;
import cn.meyoung.wlfz.webapi.entity.Car;
import cn.meyoung.wlfz.webapi.entity.Uploadfile;
import cn.meyoung.wlfz.webapi.entity.User;
import cn.meyoung.wlfz.webapi.service.CarService;
import cn.meyoung.wlfz.webapi.service.UploadfileService;
import cn.meyoung.wlfz.webapi.service.UserService;
import cn.meyoung.wlfz.webapi.service.UserTokenService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * Created by goushicai on 14-11-26.
 */

@RestController
@RequestMapping(value = "/upload")
public class UploadController {
    //上传文件的根目录
    @Value("#{settings['uploadfile.RootDir']}")
    private String uploadRoot;

    //上传文件最大长度
    @Value("#{settings['uploadfile.MaxSize']}")
    private Long upploadMaxSize;

    //下载文件的服务根目录
    @Value("#{settings['uploadfile.relativeDir']}")
    private String relativeDir;

    @Autowired
    private UploadfileService uploadfileService;

    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;

    @Autowired
    private UserTokenService userTokenService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public ApiResult getUploadfile(@Valid ListUploadfileRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }
        //非管理员只能看自己上传的文件
        //todo value="" 时，拦截请求时获取不到toke，原因未知，value="/"时可以
        if (token != null && token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            req.setUser_id(token.getUserId());
        }

        Page<Uploadfile> list = uploadfileService.list(req);
        List<UploadfileResponse> uploadfileResponse = BeanMapper.mapList(list.getContent(), UploadfileResponse.class);
        return ApiResultUtils.Paged(uploadfileResponse, list.getNumber(), list.getSize(), list.getTotalElements());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ApiResult uploadFile(@Valid AddUploadfileRequest req, Errors errors, @CurrentToken UserToken token) {
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }
        //特殊处理 ，图片上传时token在参数中提交
        UserToken param_token = userTokenService.parse(req.getToken());

        //如果token中的id与当前用户id不一致时，返回没有权限，除非当前用户是管理员角色
        if (!param_token.getUserId().equals(req.getUser_id()) && param_token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        if (req.getFile().getSize() > upploadMaxSize) {
            return ApiResultUtils.Error("999", "上传的文件大小超出限制");
        } else if (req.getFile().getSize() <= 0) {
            return ApiResultUtils.Error("999", "上传的文件为空或者未选中文件");
        }

        String ext_name = FilenameUtils.getExtension(req.getFile().getOriginalFilename());
        ext_name = "." + ext_name.toLowerCase();//tomcat url大小写敏感，有坑
        /* todo
        * 限制上传的文件类型，允许图片、声音、文档、压缩文件
        * 图片 jpg,jpeg,bmp,png,gif等
        * 声音 mp3,mid,m4a,aac,wma,amr等
        * 文档 txt,rtf,doc,xls,ppt,docx,xlsx,pptx
        * 压缩文件 zip,rar,tar,gz等
        * */

        if ("idcard,driver,driving,opt,org,biz,icon".indexOf(req.getFile_type()) >= 0 &&
                ".jpg,.jpeg,.bmp,.png,.gif".indexOf(ext_name) < 0) {
            return ApiResultUtils.Error("999", "当前的类型只允许上传图片文件");
        }

        Uploadfile uploadfile = null;

        if (req.getId() != null) {
            uploadfile = uploadfileService.find(req.getId());
            if (null == uploadfile) {
                return ApiResultUtils.NonUploadfile();
            }
        }

        if (uploadfile == null) {
            if (req.getFile_type() != "file") {
                uploadfile = uploadfileService.findByReq(req);//查找上传的文件在数据库中是否已存在
            }
        }

        if (uploadfile != null && uploadfile.getStatus() == 1) { //如果文件已经经审核，不允许覆盖
            return ApiResultUtils.UploadfileChecked();
        }

        String path = uploadRoot;
        String filename = "";

        Integer user_id = req.getUser_id();
        Integer car_id = req.getCar_id();

        /*
        *   idcard  身份证
        *   driver  驾照
        *   driving 行驶证
        *   opt     车辆营运证
        *   cargo   货源备注信息(语音，记到数据库中)
        *   org     组织代码证
        *   biz     营业执照
        *   file    普通文件
        *
         */

        if (req.getFile_type().equals("idcard")) {
            if (user_id == null) {
                return ApiResultUtils.Error("999", "上传身份证照片时，用户的id不能为空");
            }
            path += "/idcard/";
            filename = user_id.toString() + ext_name;

        } else if (req.getFile_type().equals("driver")) {
            if (user_id == null) {
                return ApiResultUtils.Error("999", "上传驾驶证照片时，用户的id不能为空");
            }
            path += "/driver/";
            filename = user_id.toString() + ext_name;

        } else if (req.getFile_type().equals("car")) {
            if (car_id == null) {
                return ApiResultUtils.Error("999", "上传车辆照片时，车辆的id不能为空");
            }
            path += "/car/";
            filename = car_id.toString() + ext_name;

        } else if (req.getFile_type().equals("driving")) {
            if (car_id == null) {
                return ApiResultUtils.Error("999", "上传行驶证照片时，车辆的id不能为空");
            }
            path += "/driving/";
            filename = car_id.toString() + ext_name;

        } else if (req.getFile_type().equals("opt")) {
            if (car_id == null) {
                return ApiResultUtils.Error("999", "上传车辆营运证照片时，车辆的id不能为空");
            }
            path += "/opt/";
            filename = car_id.toString() + ext_name;

        } else if (req.getFile_type().equals("org")) {//组织代码证
            path += "/org/";
            filename = user_id.toString() + ext_name;

        } else if (req.getFile_type().equals("biz")) {//营业执照
            path += "/biz/";
            filename = user_id.toString() + ext_name;

        } else if (req.getFile_type().equals("icon")) {//头像
            path += "/icon/";
            filename = user_id.toString() + ext_name;

        } else if (req.getFile_type().equals("cargo")) {
            req.setStatus(1);//货源相关文件不审核
            path += "/cargo/" + Integer.toString(user_id) + "/";
            filename = Long.toString(System.currentTimeMillis()) + ext_name;

        } else {
            req.setFile_type("file");
            req.setStatus(1);//普通文件不审核
            path += "/file/";
            filename = Long.toString(System.currentTimeMillis()) + ext_name;

        }
        //保存上传的文件
        String error_msg = uploadfileService.saveFile(req, path + "/" + filename);
        if (error_msg != "") {
            return ApiResultUtils.Error("999", error_msg);
        }

        //将文件信息写入数据库
        if (uploadfile == null) {
            uploadfile = new Uploadfile();
            if (req.getUser_id() != null) {
                User user = userService.find(req.getUser_id());
                uploadfile.setUser(user);
            }
            if (req.getCar_id() != null) {
                Car car = carService.find(req.getCar_id());
                uploadfile.setCar(car);
            }
        }
        uploadfile.setFile_type(req.getFile_type());
        uploadfile.setPath(relativeDir + "/" + req.getFile_type());
        uploadfile.setFile_name(filename);
        uploadfile.setUpload_time(new Date());
        uploadfile.setFile_size(req.getFile().getSize());

        if (req.getStatus() == null) {
            uploadfile.setStatus(0);
        } else {
            uploadfile.setStatus(req.getStatus());
        }

        uploadfileService.save(uploadfile);

        return ApiResultUtils.Success();
    }

    /* 图片审核 */
    @RequestMapping(value = "/{id}/check", method = RequestMethod.PUT)
    public ApiResult checkUploadFile(@PathVariable Integer id, Integer status, @CurrentToken UserToken token) {
        //管理员才能进行审核/反审核
        if (token.getIsAdmin().equals(User.IS_NOT_ADMIN)) {
            return ApiResultUtils.NoPermission();
        }

        Uploadfile uploadfile = uploadfileService.find(id);
        if (uploadfile == null) {
            return ApiResultUtils.NonUploadfile();
        }
        if (status < 0 || status > 1) {
            return ApiResultUtils.Error("999", "审核状态非法");
        }

        uploadfile.setStatus(status);
        uploadfileService.save(uploadfile);

        return ApiResultUtils.Success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResult getUploadfile(@PathVariable Integer id) {

        Uploadfile uploadfile = uploadfileService.find(id);
        if (uploadfile == null) {
            return ApiResultUtils.NonUploadfile();
        }

        UploadfileResponse res = BeanMapper.map(uploadfile, UploadfileResponse.class);

        return ApiResultUtils.Success(res);
    }
}