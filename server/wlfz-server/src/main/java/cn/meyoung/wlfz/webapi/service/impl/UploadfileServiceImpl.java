package cn.meyoung.wlfz.webapi.service.impl;

import cn.meyoung.wlfz.webapi.dao.UploadfileDao;
import cn.meyoung.wlfz.webapi.dto.request.AddUploadfileRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListUploadfileRequest;
import cn.meyoung.wlfz.webapi.entity.QUploadfile;
import cn.meyoung.wlfz.webapi.entity.Uploadfile;
import cn.meyoung.wlfz.webapi.service.UploadfileService;
import com.mysema.query.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.util.List;

/**
 * Created by goushicai on 14-11-26.
 */

@Service
public class UploadfileServiceImpl implements UploadfileService {
    @Autowired
    private UploadfileDao uploadfileDao;

    @Override
    public Uploadfile find(Integer id) {
        return uploadfileDao.findOne(id);
    }

    @Override
    public void save(Uploadfile uploadfile) {
        uploadfileDao.save(uploadfile);
    }

    @Override
    public Uploadfile findByReq(AddUploadfileRequest req) {
        List<Uploadfile> list;
        if ("car,driving,opt".indexOf(req.getFile_type()) >= 0) {
            list = uploadfileDao.findListByCarID(req.getCar_id(), req.getFile_type());

        } else {
            list = uploadfileDao.findListByUserID(req.getUser_id(), req.getFile_type());
        }
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Page<Uploadfile> list(ListUploadfileRequest request) {
        BooleanBuilder query = new BooleanBuilder();
        QUploadfile uploadfile = QUploadfile.uploadfile;

        if (request.getUser_id() != null) {
            query.and(uploadfile.user.id.eq(request.getUser_id()));
        }
        if (request.getFile_type() != null) {
            query.and(uploadfile.file_type.eq(request.getFile_type()));
        } else {
            query.and(uploadfile.file_type.ne("file"));//默认不是file类型
        }

        if (request.getStatus() != null) {
            query.and(uploadfile.status.eq(request.getStatus()));
        }

        if (request.getFile_size_from() != null) {
            query.and(uploadfile.file_size.goe(request.getFile_size_from()));
        }

        if (request.getFile_size_to() != null) {
            query.and(uploadfile.file_size.loe(request.getFile_size_to()));
        }

        if (request.getUpload_time_from() != null) {
            query.and(uploadfile.upload_time.goe(request.getUpload_time_from()));
        }

        if (request.getUpload_time_to() != null) {
            query.and(uploadfile.upload_time.loe(request.getUpload_time_to()));
        }
        //如果没有任何查询条件，默认输出未审核的列表
        if (query == null) {
            query.and(uploadfile.status.eq(0));
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageRequest = new PageRequest(request.getPage_no() - 1, request.getPage_size(), sort);

        Page<Uploadfile> uploadfilePage = uploadfileDao.findAll(query, pageRequest);

        return uploadfilePage;
    }

    @Override
    public String saveFile(AddUploadfileRequest req, String filename) {
        try {

            File file = new File(filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            FileCopyUtils.copy(req.getFile().getBytes(), file);
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
