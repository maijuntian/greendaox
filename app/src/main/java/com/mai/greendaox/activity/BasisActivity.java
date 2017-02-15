package com.mai.greendaox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mai.greendaox.XDBManager;
import com.mai.greendaox.MLog;
import com.mai.greendaox.R;
import com.mai.greendaox.bean.AllType;
import com.mai.greendaox.service.AllTypeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mai on 16/7/24.
 */
public class BasisActivity extends Activity implements View.OnClickListener {

    TextView tv_log;
    ScrollView sv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        findViewById(R.id.bt_add).setOnClickListener(this);
        findViewById(R.id.bt_delete).setOnClickListener(this);
        findViewById(R.id.bt_update).setOnClickListener(this);
        findViewById(R.id.bt_query).setOnClickListener(this);
        findViewById(R.id.bt_query_comx).setOnClickListener(this);
        findViewById(R.id.bt_exec_sql).setOnClickListener(this);
        sv_log = (ScrollView) findViewById(R.id.sv_log);
        tv_log = (TextView) findViewById(R.id.tv_log);
    }


    private void log(String text, long startTime) {
        tv_log.append(text + "，花费时间：" + (System.currentTimeMillis() - startTime) + "ms\n");
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
            case R.id.bt_query_comx:
                queryComx();
                break;
            case R.id.bt_exec_sql:
                execSql();
                break;
        }

    }

    private void insert() {
        List<AllType> allTypes = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            AllType allType = new AllType();
            allType.setTestLong(10L);
            allType.setTestBoolean(true);
            allType.setTestByte((byte) i);
            allType.setTestByteArray("test".getBytes());
            allType.setTestDate(new Date());
            allType.setTestDouble(10.0);
            allType.setTestFloat(10.0f);
            allType.setTestInt(i);
            allType.setTestShort((short) 10);
            allType.setTestString("test" + i);
            allTypes.add(allType);
        }

        long startTime = System.currentTimeMillis();
        /**如果id有重复，可以使用insertOrReplaceInTx*/
        XDBManager.getAllTypeDao().insertInTx(allTypes);
        AllType allType = new AllType();
        allType.setTestLong(11L);
        allType.setTestBoolean(true);
        allType.setTestByte((byte) 0);
        allType.setTestByteArray("test".getBytes());
        allType.setTestDate(new Date());
        allType.setTestDouble(10.0);
        allType.setTestFloat(10.0f);
        allType.setTestInt(0);
        allType.setTestShort((short) 10);
        allType.setTestString("test" + 0);

        XDBManager.getAllTypeDao().insert(allType);
        log("插入1000条数据", startTime);
    }

    private void delete() {
        long startTime = System.currentTimeMillis();
        XDBManager.getAllTypeDao().deleteAll();
        log("删除所有数据", startTime);
    }

    private void update() {
        List<AllType> allTypes = XDBManager.getAllTypeDao().loadAll();
        if (allTypes == null || allTypes.size() == 0)
            return;
        for (AllType allType : allTypes) {
            allType.setTestString("--被修改了--");
        }

        long startTime = System.currentTimeMillis();
        XDBManager.getAllTypeDao().updateInTx(allTypes);
        log("修改所有数据", startTime);
    }

    private void query() {
        long startTime = System.currentTimeMillis();
        MLog.log("查询的数据：" + XDBManager.getAllTypeDao().loadAll().toString());
        log("查询所有数据", startTime);
    }

    private void queryComx() {
        long startTime = System.currentTimeMillis();
        MLog.log("复杂查询的数据：" + new AllTypeService().getAllTypesWhereTestInt(10, 20).toString());
        log("复杂查询所有数据", startTime);
    }

    private void execSql(){
        long startTime = System.currentTimeMillis();
        new AllTypeService().deleteWhereTestInt(10, 20);
        log("执行sql语句", startTime);
    }
}
