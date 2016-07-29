package com.mai.greendaox;

import android.content.Context;

import com.mai.annotate.DataBase;
import com.mai.greendaox.bean.Car;
import com.mai.greendaox.bean.Card;
import com.mai.greendaox.bean.Children;
import com.mai.greendaox.bean.Device;
import com.mai.greendaox.bean.Parent;
import com.mai.xgreendao.base.DBManager;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by mai on 16/6/30.
 */
@DataBase(name = "greendaotest", version = 20)
public class DataBaseManager extends DBManager {

    public static AbstractDao<Device, Long> getDeviceDao(){
        return getDao(Device.class);
    }

    public static AbstractDao<Parent, Long> getParentDao(){
        return getDao(Parent.class);
    }

    public static AbstractDao<Children, Long> getChildrenDao(){
        return getDao(Children.class);
    }

    public static AbstractDao<Car, Long> getCarDao(){
        return getDao(Car.class);
    }

    public static AbstractDao<Card, String> getCardDao(){
        return getDao(Card.class);
    }
}
