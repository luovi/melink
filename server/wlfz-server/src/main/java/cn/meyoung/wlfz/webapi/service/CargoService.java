package cn.meyoung.wlfz.webapi.service;

import cn.meyoung.wlfz.webapi.dto.request.GetCargoCountRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListCargosRequest;
import cn.meyoung.wlfz.webapi.entity.Cargo;
import org.springframework.data.domain.Page;

/**
 * 货物信息服务
 *
 * @author 林守锦
 * @version 1.0
 * @date 2014/9/19
 */
public interface CargoService {

    /**
     * 获取货物信息
     *
     * @param id 货物id
     * @return 获取信息
     */
    Cargo find(Integer id);


    /**
     * 保存货物信息
     *
     * @param cargo 货物信息
     */
    void save(Cargo cargo);

    /**
     * 获取货物列表
     *
     * @param request
     * @return 货物列表
     */
    Page<Cargo> list(ListCargosRequest request);

    /**
     * 根据指定的发布时间，获取新货物信息的数量
     * @param req
     * @return 新货物数据
     */
    long getCount(GetCargoCountRequest req);
}
