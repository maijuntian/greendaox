package com.mai.greendaox;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.Table;

import java.util.List;

/**
 * Created by mai on 16/6/30.
 */
@Table(name = "usr")
public class User {
    @Id(name = "id", autoIncrement = true)
    private Long uid;

    @Column(isNull = false)
    private String name;

    @Column(isNull = true)
    private Integer sex;

    private List<Device> devices;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                '}';
    }
}
