package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.OneToOne;
import com.mai.annotate.Table;
import com.mai.greendaox.DataBaseManager;

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
    public Children children;

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

    public Children getChildren() {
        if(children == null)
            DataBaseManager.getOne(this, Children.class, id);
        return children;
    }

    public void setChildren(Children children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", date=" + date +
                '}';
    }
}
