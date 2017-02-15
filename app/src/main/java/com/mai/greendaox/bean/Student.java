package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.OneToOne;
import com.mai.annotate.Table;
import com.mai.greendaox.XDBManager;

/**
 * Created by mai on 16/7/29.
 */
@Table
public class Student {

    @Id
    String id;

    @Column
    String name;

    @Column
    int sex;

    @OneToOne
    Card card;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Card getCard() {
        if(card == null)
            card = XDBManager.getOne(this, Card.class, id);;
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", card=" + card +
                '}';
    }
}
