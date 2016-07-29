package com.mai.greendaox;

import android.app.Application;

/**
 * Created by mai on 16/7/21.
 */
public class MApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        DataBaseManager.initDataBase(this);
    }
}
