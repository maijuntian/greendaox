package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.ManyToOne;
import com.mai.annotate.Table;
import com.mai.greendaox.DataBaseManager;

/**
 * Created by mai on 16/7/13.
 */
@Table
public class Car {

    @Id(autoIncrement = true)
    Long id;

    @Column
    String name;

    @ManyToOne
    Children children;


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

    public Children getChildren() {
        if(children == null)
            children = DataBaseManager.getOne(this, Children.class, id);
        return children;
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
