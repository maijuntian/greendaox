package com.mai.greendaox;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.mai.annotate.Table;
import com.mai.xgreendao.base.BaseDataBaseManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseManager.getInstance(this);
//        User usr = new User();
//        usr.setName("test");
//        usr.setSex(1);
//        DataBaseManager.getInstance(this).getUserDao().insert(usr);
//
//        User user = new UserDaoService(this).queryByName("test");
//        if(user != null)
//            MLog.log(user.toString());
//
//        List<User> users = DataBaseManager.getInstance(this).getUserDao().loadAll();
//        MLog.log(users.toString(mnv ));


        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                DataBaseManager.getInstance(MainActivity.this).getParentDao().deleteAll();
                List<Parent> parentList = new ArrayList<Parent>();
                for (int i = 0; i < 1000; i++) {
                    Parent parent = new Parent();
                    parent.setAdmin(true);
                    parent.setDate(new java.sql.Date(1234));
                    parent.setTime(new Date());
                    parent.setEmail(i + "_@qq.com");
                    parentList.add(parent);
                }

                long time = System.currentTimeMillis();
                DataBaseManager.getInstance(MainActivity.this).getParentDao().insertInTx(parentList);
                long time2 = System.currentTimeMillis();
                MLog.log("插入花费时间：" + (time2 - time));
                List<Parent> parents = DataBaseManager.getInstance(MainActivity.this).getParentDao().loadAll();
                long time3 = System.currentTimeMillis();
                MLog.log("查询 " + parents.size() + "花费时间：" + (time3 - time2));
                return null;
            }
        }.execute();

    }
}
