package com.mai.greendaox;


import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.Table;

import java.util.Date;

/**
 * Author: wyouflf
 * Date: 13-7-25
 * Time: 下午7:06
 */
@Table(name = "parent", createIndex = "CREATE UNIQUE INDEX IDX_PARENT_NAME_EMAIL ON parent(name,email)")
public class Parent {

    @Id(name = "id", autoIncrement = true)
    private Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "email")
    private String email;

    @Column(name = "isAdmin")
    private boolean isAdmin;

    @Column(name = "time")
    private Date time;

    @Column(name = "date")
    private Date date;


    // 一对一
    //public Child getChild(DbManager db) throws DbException {
    //    return db.selector(Child.class).where("parentId", "=", this.id).findFirst();
    //}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                ", time=" + time +
                ", date=" + date +
                '}';
    }
}
