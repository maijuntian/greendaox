package com.mai.greendaox.service;

import android.content.Context;


import com.mai.greendaox.DataBaseManager;
import com.mai.greendaox.bean.Parent;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;


/**
 * Created by mai on 16/7/3.
 */
public class ParentDaoService {

    public Parent queryByName(String name){
        AbstractDao<Parent, Long> parentDao = DataBaseManager.getParentDao();
        QueryBuilder<Parent> builder = parentDao.queryBuilder();
        return builder.where(parentDao.get("sex").eq(1)).limit(1).unique();
    }
}
