package com.elvis.webDemo.core.base;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao {

    @Autowired
    private SqlSessionTemplate template;

    public int delete(String sqlmapper, Object obj) {
        return template.delete(sqlmapper, obj);
    }

    public int insert(String sqlmapper, Object obj) {
        return template.insert(sqlmapper, obj);
    }

    public <E> List<E> query(String sqlmapper, Object obj) {
        return template.selectList(sqlmapper, obj);
    }

    public int update(String sqlmapper, Object obj) {
        return template.update(sqlmapper, obj);
    }

    public <E> E queryOne(String sqlmapper, Object obj) {
        return template.selectOne(sqlmapper, obj);
    }

    public <E> List<E> query(String sqlmapper) {
        return template.selectList(sqlmapper);
    }

    public Integer queryTotal(String sqlmapper, Object obj) {
        return template.selectOne(sqlmapper, obj);
    }

    public Integer queryTotal(String sqlmapper) {
        return (Integer) template.selectOne(sqlmapper);
    }

    public HashMap<?, ?> query(String sqlmapper, Object obj, String key) {
        return (HashMap<?, ?>) template.selectMap(sqlmapper, obj, key);
    }
}
