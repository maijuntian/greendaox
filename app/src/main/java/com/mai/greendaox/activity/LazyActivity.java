package com.mai.greendaox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mai.greendaox.R;
import com.mai.greendaox.XDBManager;
import com.mai.greendaox.bean.Bag;
import com.mai.greendaox.bean.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mai on 16/8/19.
 */
public class LazyActivity extends Activity implements View.OnClickListener {

    TextView tv_log;
    ScrollView sv_log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lazy);

        sv_log = (ScrollView) findViewById(R.id.sv_log);
        tv_log = (TextView) findViewById(R.id.tv_log);

        initData();

        findViewById(R.id.bt_query).setOnClickListener(this);
    }

    private void log(String text, long startTime) {
        tv_log.append("花费时间：" + (System.currentTimeMillis() - startTime) + "ms\n");
        tv_log.append("查询结果：" + text +"\n");
        sv_log.fullScroll(View.FOCUS_DOWN);
    }

    public void initData() {
        XDBManager.getBagDao().deleteAll();
        XDBManager.getBookDao().deleteAll();

        Bag bag = new Bag();
        bag.setName("书包");

        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Book book = new Book();
            book.setName("高数" + i);
            book.setPage(100 + i);
            book.setBag(bag);
            books.add(book);
        }

        XDBManager.getBagDao().insert(bag);
        XDBManager.getBookDao().insertInTx(books);
    }

    @Override
    public void onClick(View v) {
        Long startTime  = System.currentTimeMillis();
        List<Bag> bags = XDBManager.getBagDao().loadAll();
        for (Bag bag : bags){
            log(bag.toString(), startTime);
        }
    }
}
