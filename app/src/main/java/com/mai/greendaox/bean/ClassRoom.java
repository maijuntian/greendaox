package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.ManyToOne;
import com.mai.annotate.Table;
import com.mai.greendaox.XDBManager;

/**
 * Created by mai on 16/8/5.
 */
@Table
public class ClassRoom {

    @Id
    private Long id;

    @Column
    private String name;


    @ManyToOne
    private School school;

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

    public School getSchool() {
        if(school == null){
            school = XDBManager.getOne(this, School.class, id);
        }
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
