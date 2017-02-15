package com.mai.greendaox.bean;

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
public class Boss {

    @Id(autoIncrement = true)
    private Long id;

    @Column
    String name;

    @Column
    String company;

    @OneToMany(type = "com.mai.greendaox.bean.Employees")
    List<Employees> employees;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<Employees> getEmployees() {
        if(employees == null){
            employees = XDBManager.getMany(this, Employees.class, id);
        }
        return employees;
    }

    public void setEmployees(List<Employees> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Boss{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", company=" + company +
                ", employees=" + employees +
                '}';
    }
}
