package cn.meyoung.wlfz.webapi.controller;

import cn.meyoung.wlfz.webapi.KeyWords;
import cn.meyoung.wlfz.webapi.dto.ApiResult;
import cn.meyoung.wlfz.webapi.dto.ApiResultUtils;
import cn.meyoung.wlfz.webapi.dto.request.AddNoticeRequest;
import cn.meyoung.wlfz.webapi.dto.request.AddSearchRecordRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListNoticeRequest;
import cn.meyoung.wlfz.webapi.dto.request.ListSearchRecordRequest;
import cn.meyoung.wlfz.webapi.dto.response.KeyWordsResponse;
import cn.meyoung.wlfz.webapi.dto.response.NoticeResponse;
import cn.meyoung.wlfz.webapi.dto.response.SearchRecordResponse;
import cn.meyoung.wlfz.webapi.entity.Notice;
import cn.meyoung.wlfz.webapi.entity.SearchRecord;
import cn.meyoung.wlfz.webapi.service.SearchRecordService;
import cn.meyoung.wlfz.webapi.utils.BeanMapper;
import cn.meyoung.wlfz.webapi.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by shaowei on 2015/1/6.
 *  搜索关键字
 */
@RestController
@RequestMapping("/searchrecords")
public class SearchRecordController extends BaseController {
    @Autowired
    private SearchRecordService searchRecordService;

    /**
     * 记录搜索的关键字。。
     *
     * @param request
     * @param errors
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ApiResult addKeyword(@Valid AddSearchRecordRequest request, Errors errors) {
        //@Valid注解会对request参数格式验证
        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

        SearchRecord notice = BeanMapper.map(request, SearchRecord.class);


        //添加参数，要存入数据库表字段的; 要验证默认值
        //默认当前时间。。
        if (null == request.getQueryTime()) {
            notice.setQueryTime(new Date());
        }


        searchRecordService.Save(notice);

        //自定义
        return ApiResultUtils.Success();
    }


    /**
     * 获取列表。。
     *
     * @param req
     * @param errors
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ApiResult getKeywords(@Valid ListSearchRecordRequest req, Errors errors) {

        if (errors.hasErrors()) {
            return ApiResultUtils.ErrorParameter();
        }

     /*  也可以对输入的请求条件参数进一步判断
        if (req.getBegin_time() == null && req.getEnd_time() == null) {
            req.setEnd_time(new Date());
        }*/

        Page<SearchRecord> list = searchRecordService.List(req);
        //BeanMapper自定义的类，其中关联dozerBeanMapping.xml
        List<SearchRecordResponse> noticeResponses = BeanMapper.mapList(list.getContent(), SearchRecordResponse.class);

        //自定义
        return ApiResultUtils.Paged(noticeResponses, list.getNumber(), list.getSize(), list.getTotalElements());
    }


    /**
     * 获取指定数量的分组列表
     * count是每次返回多少；
     * whichpage是从第几页开始
     *
     * @param
     * @param
     * @return
     */
    @RequestMapping(value = "/hot", method = RequestMethod.GET)
    public ApiResult getHotKeywords(Integer count, Integer whichpage) {


     /*  也可以对输入的请求条件参数进一步判断*/
        if (null == count || !RequestUtils.isNumeric(count.toString())) {
            count = 10;//默认查询10条
        }
        if (null == whichpage || !RequestUtils.isNumeric(whichpage.toString())) {
            whichpage = 1;//默认从第一页
        }

        List result = searchRecordService.getKeyWordList(count, whichpage);
        List<KeyWords> words = new ArrayList<>();
        KeyWords key;

        Iterator iterator = result.iterator();
        while (iterator.hasNext()) {
            key = new KeyWords();

            Object[] row = (Object[]) iterator.next();

            Long countid = Long.parseLong(row[0].toString());
            key.setCount(countid);
            String PersonName = row[1].toString();
            key.setKeyword(PersonName);

            words.add(key);

        }

        //自定义
        List<KeyWordsResponse> noticeResponses = BeanMapper.mapList(words, KeyWordsResponse.class);

        //自定义
        return ApiResultUtils.Success(noticeResponses);
    }


    @RequestMapping(value = "/hotnew", method = RequestMethod.GET)
    public ApiResult getHotKeywordsnew(Integer count, Integer whichpage) {


     /*  也可以对输入的请求条件参数进一步判断*/
        if (null == count || !RequestUtils.isNumeric(count.toString())) {
            count = 10;//默认查询10条
        }
        if (null == whichpage || !RequestUtils.isNumeric(whichpage.toString())) {
            whichpage = 1;//默认从第一页
        }

        List<KeyWords> result = searchRecordService.getKeyWordGroup(count, whichpage);


        //自定义
        return ApiResultUtils.Success(result);
    }


    @RequestMapping(value = "/hots", method = RequestMethod.GET)
    public ApiResult getHotKeywords(Integer count) {


     /*  也可以对输入的请求条件参数进一步判断*/
        if (null == count || !RequestUtils.isNumeric(count.toString())) {
            count = 10;//默认查询10条
        }

        List result = searchRecordService.getKeyWordByCount();
        List<KeyWords> words = new ArrayList<>();
        //循环给keyword负值
        if (result != null && result.size() <= count) {

            KeyWords key;
            Iterator iterator = result.iterator();

            while (iterator.hasNext()) {
                key = new KeyWords();

                Object[] row = (Object[]) iterator.next();

                Long countid = Long.parseLong(row[0].toString());
                key.setCount(countid);
                String PersonName = row[1].toString();
                key.setKeyword(PersonName);
                words.add(key);

            }
        } else if (result != null && result.size() > count) {
            KeyWords key;
            Iterator iterator = result.iterator();
            int flag = 1;

            while (iterator.hasNext()) {
                key = new KeyWords();

                Object[] row = (Object[]) iterator.next();

                Long countid = Long.parseLong(row[0].toString());
                key.setCount(countid);
                String PersonName = row[1].toString();
                key.setKeyword(PersonName);
                words.add(key);

                flag++;
                if (flag > count)
                    break;

            }
        }

        List<KeyWordsResponse> noticeResponses = BeanMapper.mapList(words, KeyWordsResponse.class);

        //自定义
        return ApiResultUtils.Success(noticeResponses);//可以返回所有的，可以进一步根据count输出前几条记录。
        //   return ApiResultUtils.Paged(noticeResponses, list.getNumber(),count, list.getTotalElements());
    }


}
