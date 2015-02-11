
package cn.meyoung.wlfz.webapi.dao;

import cn.meyoung.wlfz.webapi.KeyWords;

import javax.persistence.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by shaowei on 2015/1/6.
 */

public class SearchRecordDaoImpl implements SearchRecordDaoCustomize {


    @PersistenceContext//(unitName = "JPA")
    private EntityManager em;

    public List getKeyWordGroupList(int max, int whichpage) {
        // EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        //  EntityManager em = factory.createEntityManager();

        int index = (whichpage - 1) * max;

        Query query = em.createQuery("select count(t),t.keyword from SearchRecord t group by t.keyword order by count(t) desc");

      /*  Query query = em.createQuery(" select p.sex, count(p) from Person p group by p.sex ");
// 集合中的元素不再是 Person, 而是一个 Object[] 对象数组
        List result = query.getResultList();*/
        List list = query.setMaxResults(max).
                setFirstResult(index).
                getResultList();

        em.clear(); // 分离内存中受EntityManager管理的实体bean，让VM进行垃圾回收

        return list;

    }

    public List<KeyWords> getKeyWordGroupPage(int max, int whichpage) {
       /* EntityManagerFactory factory = Persistence.createEntityManagerFactory("words");
        EntityManager em = factory.createEntityManager();*/
        int index = (whichpage - 1) * max;

        Query query = em.createQuery("select new cn.meyoung.wlfz.webapi.KeyWords(count(t),t.keyword) " +
                "from SearchRecord t group by t.keyword order by count(t) desc");
        // 集合中的元素是 KeyWords 对象
        List result = query.setMaxResults(max).
                setFirstResult(index).
                getResultList();

/* for(Abc abc : abcs){
            System.out.println("imsi:"+abc.getImsi());
            System.out.println("sipss:"+abc.getSipss());
            System.out.println("mdn:"+abc.getMdn());
            System.out.println("========");
        }*/


        em.close();

        // factory.close();
        List<KeyWords> list = new ArrayList<>();
        if (result != null) {

            Iterator iterator = result.iterator();

            while (iterator.hasNext()) {

                KeyWords words = (KeyWords) iterator.next();

                list.add(words);
            }

        }

        return list;
    }

}

