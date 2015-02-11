package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.AddUploadfileRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListUploadfileRequest;
import cn.meyoung.wlfz.webapi.entity.Uploadfile;
import org.springframework.data.domain.Page;

/**
 * Created by goushicai on 14-11-26.
 */
public interface UploadfileService {
    Uploadfile find(Integer id);

    void save(Uploadfile uploadfile);

    Page<Uploadfile> list(ListUploadfileRequest request);

    String saveFile(AddUploadfileRequest req, String filename);

    Uploadfile findByReq(AddUploadfileRequest req);
}
