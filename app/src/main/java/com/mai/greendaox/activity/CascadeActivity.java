package com.mai.greendaox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mai.greendaox.XDBManager;
import com.mai.greendaox.MLog;
import com.mai.greendaox.R;
import com.mai.greendaox.bean.ClassRoom;
import com.mai.greendaox.bean.School;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mai on 16/7/29.
 */
public class CascadeActivity extends Activity implements View.OnClickListener {

    private String tag = "级联操作";

    TextView tv_log;
    ScrollView sv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation);

        findViewById(R.id.bt_add).setOnClickListener(this);
        findViewById(R.id.bt_delete).setOnClickListener(this);
        findViewById(R.id.bt_update).setOnClickListener(this);
        findViewById(R.id.bt_query).setOnClickListener(this);
        sv_log = (ScrollView) findViewById(R.id.sv_log);
        tv_log = (TextView) findViewById(R.id.tv_log);
    }


    private void log(String text, long startTime) {
        tv_log.append(tag + text + "，花费时间：" + (System.currentTimeMillis() - startTime) + "ms\n");
        sv_log.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                insert();
                break;
            case R.id.bt_delete:
                delete();
                break;
            case R.id.bt_update:
                update();
                break;
            case R.id.bt_query:
                query();
                break;
        }

    }

    private void update() {
        long startTime = System.currentTimeMillis();
        List<School> schools = XDBManager.getSchoolDao().loadAll();
        if (schools != null) {
            for (School school : schools){
                school.setName(school + "-修改后");
                List<ClassRoom> classRooms = school.getClassRooms();
                if(classRooms != null){
                    for (ClassRoom classRoom : classRooms){
                        classRoom.setName(classRoom.getName() + "-修改后");
                    }
                }
                XDBManager.getSchoolDao().update(school);
            }
        }
        log("级联更新", startTime);
    }

    private void query() {
        long startTime = System.currentTimeMillis();
        List<School> schools = XDBManager.getSchoolDao().loadAll();
        log("查询数据", startTime);
        if (schools != null) {
            for (School s : schools) {
                MLog.log(s.toString());
                MLog.log(s.getClassRooms().toString());
            }
        }
    }

    private void delete() {
        long startTime = System.currentTimeMillis();
        XDBManager.getSchoolDao().deleteAll();
        log("级联删除", startTime);
    }

    private void insert() {
        School school = new School();
        List<ClassRoom> classRooms = new ArrayList<>();
        school.setName("清华大学");

        for (int i = 0; i < 10; i++) {
            ClassRoom classRoom = new ClassRoom();
            classRoom.setName("计算机11" + i + "班");
            classRoom.setSchool(school);

            classRooms.add(classRoom);
        }

        school.setClassRooms(classRooms);

        long startTime = System.currentTimeMillis();
        XDBManager.getSchoolDao().insertInTx(school);
        log("级联插入", startTime);
    }
}
