package com.mai.xgreendao.base;

import android.content.Context;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by mai on 16/6/30.
 */
public class BaseDataBaseManager {
    public AbstractDaoSession daoSession;
    public final static String DATABASE_MANAGER_CLASS = "com.mai.xgreendao.base.BaseDataBaseManagerImpl";

    public interface SessionInter {
        AbstractDaoSession getDaoSession(Context context);
    }

    public BaseDataBaseManager(Context context) {
        try {
            SessionInter inter = (SessionInter) Class.forName(DATABASE_MANAGER_CLASS).newInstance();
            daoSession = inter.getDaoSession(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T, K> AbstractDao<T, K> getDao(Class<T> clazz) {
        return (AbstractDao<T, K>) daoSession.getDao(clazz);
    }

    public AbstractDaoSession getDaoSession() {
        return daoSession;
    }

}
