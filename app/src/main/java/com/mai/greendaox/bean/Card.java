package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.OneToOne;
import com.mai.annotate.Table;
import com.mai.greendaox.XDBManager;

import java.util.Date;

/**
 * Created by mai on 16/7/13.
 */
@Table
public class Card {
    @Id
    private String id;

    @Column
    private Date date;

    @OneToOne
    public Student student;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Student getStudent() {
        if(student == null)
            student = XDBManager.getOne(this, Student.class, id);
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", date=" + date +
                '}';
    }
}
