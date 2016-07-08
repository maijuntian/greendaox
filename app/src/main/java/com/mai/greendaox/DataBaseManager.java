package com.mai.greendaox;

import android.content.Context;

import com.mai.annotate.DataBase;
import com.mai.xgreendao.base.BaseDataBaseManager;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by mai on 16/6/30.
 */
@DataBase(name = "greendaotest", version = 8)
public class DataBaseManager extends BaseDataBaseManager{
    public static DataBaseManager instance;

    public DataBaseManager(Context context) {
        super(context);
    }

    public static DataBaseManager getInstance(Context context){
        if(instance == null){
            instance = new DataBaseManager(context);
        }
        return instance;
    }

    public AbstractDao<User, Long> getUserDao(){
        return getDao(User.class);
    }

    public AbstractDao<Device, Long> getDeviceDao(){
        return getDao(Device.class);
    }

    public AbstractDao<Parent, Long> getParentDao(){
        return getDao(Parent.class);
    }
}
