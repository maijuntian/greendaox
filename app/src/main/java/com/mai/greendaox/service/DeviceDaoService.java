package com.mai.greendaox.service;

import android.content.Context;

import com.mai.greendaox.DataBaseManager;
import com.mai.greendaox.bean.Device;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by mai on 16/7/3.
 */
public class DeviceDaoService {


    public List<Device> queryByTestString(String testString){
        AbstractDao<Device, Long> deviceDao = DataBaseManager.getDeviceDao();
        QueryBuilder<Device> builder = deviceDao.queryBuilder();
        return builder.where(builder.and(deviceDao.get("testString").eq(testString), deviceDao.get("testLong").notIn(1, 3))).list();
    }
}
