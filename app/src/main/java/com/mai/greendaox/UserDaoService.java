package com.mai.greendaox;

import android.content.Context;


import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by mai on 16/7/3.
 */
public class UserDaoService {

    private AbstractDao<User, Long> userDao;

    public UserDaoService(Context context){
        userDao = DataBaseManager.getInstance(context).getUserDao();
    }

    public User queryByName(String name){
        QueryBuilder<User> builder = userDao.queryBuilder();
        return builder.where(userDao.get("sex").eq(1)).limit(1).unique();
    }
}
