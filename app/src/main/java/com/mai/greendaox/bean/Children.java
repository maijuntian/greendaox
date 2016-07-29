package com.mai.greendaox.bean;

import com.mai.annotate.Cascade;
import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.ManyToMany;
import com.mai.annotate.ManyToOne;
import com.mai.annotate.OneToMany;
import com.mai.annotate.OneToOne;
import com.mai.annotate.Table;
import com.mai.greendaox.DataBaseManager;

import java.util.List;

/**
 * Created by mai on 16/7/13.
 */
@Table
public class Children {
    @Id(autoIncrement = true)
    private Long id;

    @Column
    private String name;

    @Column
    private int sex;

    @ManyToMany(type = "com.mai.greendaox.bean.Parent", inverse = false, lazy = false, cascade = {Cascade.INSERT, Cascade.INSERT_OR_REPLACE, Cascade.UPDATE, Cascade.DELETE})
    private List<Parent> parents;

    @OneToMany(type = "com.mai.greendaox.bean.Car", lazy = false, cascade = {Cascade.INSERT, Cascade.INSERT_OR_REPLACE, Cascade.UPDATE, Cascade.DELETE})
    private List<Car> cars;

    @OneToOne(cascade = {Cascade.INSERT, Cascade.INSERT_OR_REPLACE, Cascade.UPDATE, Cascade.DELETE}, lazy = false)
    private Card card;

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

    public List<Parent> getParents() {
        if(parents == null){
            parents = DataBaseManager.getMany(this, Parent.class, id);
        }
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    public List<Car> getCars() {
        if(cars == null) {
            cars = DataBaseManager.getMany(this, Car.class, id);
        }
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Card getCard() {
        if(card == null)
            card = DataBaseManager.getOne(this, Card.class, id);
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "Children{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", parents=" + parents +
                ", cars=" + cars +
                ", card=" + card +
                '}';
    }
}
