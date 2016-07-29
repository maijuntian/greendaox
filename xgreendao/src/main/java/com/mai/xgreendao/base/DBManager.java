package com.mai.xgreendao.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by mai on 16/6/30.
 */
public class DBManager {
    protected static AbstractDaoSession daoSession;
    private final static String DATABASE_MANAGER_CLASS = "com.mai.xgreendao.base.DBManagerImpl";

    public interface SessionInter {
        AbstractDaoSession getDaoSession(Context context);
    }

    public static void initDataBase(Context context) {
        try {
            SessionInter inter = (SessionInter) Class.forName(DATABASE_MANAGER_CLASS).newInstance();
            daoSession = inter.getDaoSession(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  <T, K> AbstractDao<T, K> getDao(Class<T> clazz) {
        checkDaoSession();
        return (AbstractDao<T, K>) daoSession.getDao(clazz);
    }

    private static void checkDaoSession() {
        if (daoSession == null)
            throw new IllegalStateException("daoSession is null, please init the Database at first!");
    }

    public static AbstractDaoSession getDaoSession() {
        return daoSession;
    }

    public static SQLiteDatabase getDatabase(){
        checkDaoSession();
        return daoSession.getDatabase();
    }

    /**
     * 清除缓存
     */
    public static void clear(){
        daoSession.clear();
    }

    /**
     * 获取一对多或多对多，多的一方数据
     * @param bean 自身引用
     * @param targetClass 目标对象的class
     * @param id 主键
     * @param <T>
     * @return
     */
    public static <T> List<T> getMany(Object bean, Class<T> targetClass, Object id) {
        if(id == null)
            return null;
        checkDaoSession();
        AbstractDao<?, ?> dao = daoSession.getDao(bean.getClass());
        try {
            Method method = dao.getClass().getMethod("load" + targetClass.getSimpleName() + "s", id.getClass());
            if (method != null) {
                return (List<T>) method.invoke(dao, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取多对一或者一对一，一的一方数据
     * @param bean 自身引用
     * @param targetClass  目标对象class
     * @param id  主键
     * @param <T>
     * @return
     */
    public static <T> T getOne(Object bean, Class<T> targetClass, Object id){
        if(id == null)
            return null;
        checkDaoSession();
        AbstractDao<?, ?> dao = daoSession.getDao(bean.getClass());
        try {
            Method method = dao.getClass().getMethod("load" + targetClass.getSimpleName(), id.getClass());
            if (method != null) {
                return (T) method.invoke(dao, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
