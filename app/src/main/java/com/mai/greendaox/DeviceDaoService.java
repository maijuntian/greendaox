package com.mai.greendaox;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by mai on 16/7/3.
 */
public class DeviceDaoService {

    private AbstractDao<Device, Long> deviceDao;

    public DeviceDaoService(Context context){
        deviceDao = DataBaseManager.getInstance(context).getDeviceDao();
    }

    public List<Device> queryByTestString(String testString){
        QueryBuilder<Device> builder = deviceDao.queryBuilder();
        return builder.where(builder.and(deviceDao.get("testString").eq(testString), deviceDao.get("testLong").notIn(1, 3))).list();
    }
}
