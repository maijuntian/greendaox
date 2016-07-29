package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.ManyToMany;
import com.mai.annotate.OneToMany;
import com.mai.annotate.Table;
import com.mai.greendaox.DataBaseManager;

import java.util.List;

/**
 * Created by mai on 16/7/13.
 */
@Table
public class Parent {
    @Id(autoIncrement = true)
    private Long id;

    @Column
    private String name;

    @Column
    private int sex;

    @Column
    private String address;

    @ManyToMany(type = "com.mai.greendaox.bean.Children", inverse = true, lazy = false)
    private List<Children> childrens;

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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Children> getChildrens() {
        if(childrens == null){
            childrens = DataBaseManager.getMany(this, Children.class, id);
        }
        return childrens;
    }

    public void setChildrens(List<Children> childrens) {
        this.childrens = childrens;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", address='" + address + '\'' +
                '}';
    }
}
