package com.mai.greendaox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mai.greendaox.XDBManager;
import com.mai.greendaox.MLog;
import com.mai.greendaox.R;
import com.mai.greendaox.bean.Card;
import com.mai.greendaox.bean.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by mai on 16/7/29.
 */
public class OneToOneActivity extends Activity implements View.OnClickListener {

    private String tag = "一对一";

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
        Student student = XDBManager.getStudentDao().load("SS201601012");
        if(student != null) {
            student.setName("-update-");
            XDBManager.getStudentDao().update(student);

            Card card = student.getCard();
            if(card != null){
                Calendar calendar = Calendar.getInstance();
                calendar.set(1992,11,21);
                card.setDate(calendar.getTime());
                XDBManager.getCardDao().update(card);
            }
        }
        log("更新数据", startTime);
    }

    private void query() {
        long startTime = System.currentTimeMillis();
        List<Student> students = XDBManager.getStudentDao().loadAll();
        log("查询数据", startTime);
        if(students != null){
            for (Student s : students) {
                MLog.log(s.toString());
                MLog.log(s.getCard().toString());
            }
        }
    }

    private void delete() {
        long startTime = System.currentTimeMillis();
        XDBManager.getStudentDao().deleteAll();
        XDBManager.getCardDao().deleteAll();
        log("删除数据", startTime);
    }

    private void insert() {
        List<Student> students = new ArrayList<>();
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            student.setId("SS20160101" + i);
            student.setName("哈哈" + i);
            student.setSex(1);
            Card card = new Card();
            card.setId("CC20160101" + i);
            card.setDate(new Date());

            card.setStudent(student);
            student.setCard(card);

            students.add(student);
            cards.add(card);
        }

        long startTime = System.currentTimeMillis();
        XDBManager.getStudentDao().insertInTx(students);
        XDBManager.getCardDao().insertInTx(cards);
        log("插入数据", startTime);
    }
}
