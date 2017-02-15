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
public class Order {
    @Id(autoIncrement = true)
    private Long id;

    @Column
    private String address;


    @ManyToMany(type = "com.mai.greendaox.bean.Product", inverse = true /**设置Order维护Porduct关系*/)
    private List<Product> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Product> getProducts() {
        if(products == null){
            products = XDBManager.getMany(this, Product.class, id);
        }
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", products=" + products +
                '}';
    }
}
