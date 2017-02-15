package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.ManyToOne;
import com.mai.annotate.Table;

/**
 * Created by mai on 16/8/19.
 */
@Table
public class Book {

    @Id(autoIncrement = true)
    private Long id;

    @Column
    private String name;

    @Column
    private int page;

    @ManyToOne(lazy = false /**取消懒加载，当你查询Book数据的时候，bag的数据也会同时加载*/)
    private Bag bag;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Bag getBag() {
        return bag;
    }

    public void setBag(Bag bag) {
        this.bag = bag;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", page=" + page +
                '}';
    }
}
