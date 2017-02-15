package com.mai.greendaox.bean;

import com.mai.annotate.Cascade;
import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.OneToMany;
import com.mai.annotate.Table;
import com.mai.greendaox.XDBManager;

import java.util.List;

/**
 * Created by mai on 16/8/5.
 */
@Table
public class School {

    @Id(autoIncrement = true)
    private Long id;

    @Column
    private String name;

    @OneToMany(type = "com.mai.greendaox.bean.ClassRoom", cascade = {Cascade.INSERT, Cascade.INSERT_OR_REPLACE, Cascade.UPDATE, Cascade.DELETE})
    private List<ClassRoom> classRooms;

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

    public List<ClassRoom> getClassRooms() {
        if(classRooms == null){
            classRooms = XDBManager.getMany(this, ClassRoom.class, id);
        }
        return classRooms;
    }

    public void setClassRooms(List<ClassRoom> classRooms) {
        this.classRooms = classRooms;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", classRooms=" + classRooms +
                '}';
    }
}
