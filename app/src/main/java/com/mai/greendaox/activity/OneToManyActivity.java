package com.mai.greendaox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mai.greendaox.XDBManager;
import com.mai.greendaox.MLog;
import com.mai.greendaox.R;
import com.mai.greendaox.bean.Boss;
import com.mai.greendaox.bean.Employees;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mai on 16/7/29.
 */
public class OneToManyActivity extends Activity implements View.OnClickListener {

    private String tag = "一对多";


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
        List<Boss> bosses = XDBManager.getBossDao().loadAll();
        if(bosses != null){
            for (Boss boss : bosses){
                boss.setCompany(boss.getCompany() + "-修改后");
                List<Employees> employeesList = boss.getEmployees();
                if(employeesList != null){
                    for (Employees employees : employeesList){
                        employees.setName(employees.getName() + "-修改后");
                    }
                }
                XDBManager.getBossDao().update(boss);
                XDBManager.getEmployeesDao().updateInTx(employeesList);
            }
        }
        log("更新数据", startTime);
    }

    private void query() {
        long startTime = System.currentTimeMillis();
        List<Boss> bosses = XDBManager.getBossDao().loadAll();
        log("查询数据", startTime);
        if(bosses != null){
            for (Boss b : bosses) {
                MLog.log(b.toString());
                MLog.log(b.getEmployees().toString());
            }
        }
    }

    private void delete() {
        long startTime = System.currentTimeMillis();
        XDBManager.getBossDao().deleteAll();
        XDBManager.getEmployeesDao().deleteAll();
        log("删除数据", startTime);
    }

    private void insert() {
        List<Boss> bosses = new ArrayList<>();
        Boss boss1 = new Boss();
        boss1.setName("麦俊填");
        boss1.setCompany("互联网技术有限公司");
        bosses.add(boss1);

        Boss boss2 = new Boss();
        boss2.setName("马云");
        boss2.setCompany("阿里巴巴");
        bosses.add(boss2);

        List<Employees> employees = new ArrayList<>();
        for(int i = 0; i < 20; i ++){
            Employees employee = new Employees();
            employee.setName("员工：" + i);
            employee.setDepartment("开发部门");
            if(i % 2 == 0){
                employee.setId("Mai2016X" + i);
                employee.setBoss(boss1);
            } else{
                employee.setId("Ma2016X" + i);
                employee.setBoss(boss2);
            }
            employees.add(employee);
        }

        long startTime = System.currentTimeMillis();
        /**
         * 因为一对多是由多的一边维护关系的，所以在插入empoyees之前需要先插入bosses，
         * 因为bosses 的id是自增的，插入后才有id,在插入empoyees时才能进行维护bosses关系
         */
        XDBManager.getBossDao().insertInTx(bosses);
        XDBManager.getEmployeesDao().insertInTx(employees);
        log("插入数据", startTime);
    }
}
