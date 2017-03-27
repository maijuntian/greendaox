package com.mai.xgreendao.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by mai on 16/6/30.
 */
public class DBManager {
//    protected static AbstractDaoSession daoSession;

    private final static String DATABASE_MANAGER_CLASS = "com.mai.xgreendao.base.DBManagerImpl";

    static SparseArray<AbstractDaoSession> daoSessionMap = new SparseArray<>();

    public interface SessionInter {
        AbstractDaoSession getDaoSession(Context context);
    }

    public static void initDataBase(Context context) {
        initDataBase(context, 0);
    }

    public static void initDataBase(Context context, int dbKey) {
        try {
            SessionInter inter = (SessionInter) Class.forName(DATABASE_MANAGER_CLASS + dbKey).newInstance();
            daoSessionMap.put(dbKey,inter.getDaoSession(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  <T, K> AbstractDao<T, K> getDao(Class<T> clazz) {
        return getDao(clazz, 0);
    }

    public static  <T, K> AbstractDao<T, K> getDao(Class<T> clazz, int dbKey) {
        checkDaoSession();
        return (AbstractDao<T, K>) daoSessionMap.get(dbKey).getDao(clazz);
    }

    private static void checkDaoSession() {
        if (daoSessionMap.size() == 0)
            throw new IllegalStateException("daoSession is null, please init the Database at first!");
    }

    public static AbstractDaoSession getDaoSession() {
        return getDaoSession(0);
    }

    public static AbstractDaoSession getDaoSession(int dbKey) {
        return daoSessionMap.get(dbKey);
    }

    public static SQLiteDatabase getDatabase(){
        return getDatabase(0);
    }

    public static SQLiteDatabase getDatabase(int dbKey){
        checkDaoSession();
        return daoSessionMap.get(dbKey).getDatabase();
    }

    /**
     * 清除缓存
     */
    public static void clear(){
        clear(0);
    }

    public static void clear(int dbKey){
        daoSessionMap.get(dbKey).clear();
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
        return getMany(bean, targetClass, id, 0);
    }

    /**
     * 获取一对多或多对多，多的一方数据
     * @param bean 自身引用
     * @param targetClass 目标对象的class
     * @param id 主键
     * @param <T>
     * @return
     */
    public static <T> List<T> getMany(Object bean, Class<T> targetClass, Object id, int dbKey) {
        if(id == null)
            return null;
        checkDaoSession();
        AbstractDao<?, ?> dao = daoSessionMap.get(dbKey).getDao(bean.getClass());
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
        return getOne(bean, targetClass, id, 0);
    }


    /**
     * 获取多对一或者一对一，一的一方数据
     * @param bean 自身引用
     * @param targetClass  目标对象class
     * @param id  主键
     * @param <T>
     * @return
     */
    public static <T> T getOne(Object bean, Class<T> targetClass, Object id, int dbKey){
        if(id == null)
            return null;
        checkDaoSession();
        AbstractDao<?, ?> dao = daoSessionMap.get(dbKey).getDao(bean.getClass());
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
