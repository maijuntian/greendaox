package com.mai.greendaox.service;

import com.mai.greendaox.XDBManager;
import com.mai.greendaox.bean.AllType;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by mai on 16/7/29.
 */
public class AllTypeService {

    public List<AllType> getAllTypesWhereTestInt(int from, int end) {
        AbstractDao<AllType, Long> dao = XDBManager.getAllTypeDao();
        QueryBuilder<AllType> qb = dao.queryBuilder();

        /** key为 bean中的属性名*/
        return qb.where(dao.get("testInt").between(from, end)).list();
    }

    public void deleteWhereTestInt(int from, int end){
        XDBManager.getDatabase().execSQL("delete from all_type where testInt between " + from + " and " + end);
//        XDBManager.getDatabase().delete("all_type", "testInt between ? and ?", new String[]{from+"", end+""});
    }
}
