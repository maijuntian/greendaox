package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.ManyToMany;
import com.mai.annotate.Table;
import com.mai.greendaox.XDBManager;

import java.util.List;

/**
 * Created by mai on 16/8/5.
 */
@Table
public class Product {
    @Id(autoIncrement = true)
    private Long id;

    @Column
    private String name;

    @Column
    private float price;

    @ManyToMany(type = "com.mai.greendaox.bean.Order", inverse = false /**设置Product不维护Order关系*/)
    List<Order> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<Order> getOrders() {
        if(orders == null){
            orders = XDBManager.getMany(this, Order.class, id);
        }
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
