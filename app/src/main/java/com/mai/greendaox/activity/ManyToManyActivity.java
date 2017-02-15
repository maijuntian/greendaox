package com.mai.greendaox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mai.greendaox.XDBManager;
import com.mai.greendaox.MLog;
import com.mai.greendaox.R;
import com.mai.greendaox.bean.Order;
import com.mai.greendaox.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mai on 16/7/29.
 */
public class ManyToManyActivity extends Activity implements View.OnClickListener {

    private String tag = "多对多";


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
        List<Order> orders = XDBManager.getOrderDao().loadAll();
        if(orders != null){
            for (Order order : orders){
                order.setAddress(order.getAddress() + "-修改后");
                List<Product> productList = order.getProducts();
                if(productList != null){
                    for (Product product : productList){
                        product.setName(product.getName() + "-修改后");
                    }
                }
                XDBManager.getProductDao().updateInTx(productList);
                XDBManager.getOrderDao().update(order);
            }
        }
        log("更新数据", startTime);
    }

    private void query() {
        long startTime = System.currentTimeMillis();
        List<Order> orders = XDBManager.getOrderDao().loadAll();
        log("查询数据", startTime);
        if(orders != null){
            for (Order o : orders) {
                MLog.log(o.toString());
                MLog.log(o.getProducts().toString());
            }
        }
    }

    private void delete() {
        long startTime = System.currentTimeMillis();
        XDBManager.getProductDao().deleteAll();
        XDBManager.getOrderDao().deleteAll();
        log("删除数据", startTime);
    }

    private void insert() {
        List<Product> products = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        for (int i= 0; i < 10; i++){
            Product product = new Product();
            product.setName("机器人" + i + "号");
            product.setPrice(10000f + i);
            products.add(product);
        }

        for (int i = 0 ; i < 3; i ++){
            Order order = new Order();
            order.setAddress("广州广州塔");

            List<Product> pList = new ArrayList<>();
            pList.add(products.get(i));
            pList.add(products.get(i+1));
            pList.add(products.get(i+2));
            pList.add(products.get(i+3));
            pList.add(products.get(i+4));
            order.setProducts(pList);

            orders.add(order);
        }

        long startTime = System.currentTimeMillis();
        /**
         * 因为多对多设置了由order维护关系的，所以在插入order之前需要先插入product，
         * 因为product 的id是自增的，插入后才有id,在插入order时才能进行维护product关系
         */
        XDBManager.getProductDao().insertInTx(products);
        XDBManager.getOrderDao().insertInTx(orders);
        log("插入数据", startTime);
    }
}
