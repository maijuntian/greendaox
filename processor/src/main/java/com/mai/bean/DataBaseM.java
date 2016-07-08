package com.mai.bean;

/**
 * 数据库模型
 * Created by mai on 16/6/30.
 */
public class DataBaseM {
    private String name;
    private int version;

    public DataBaseM() {
    }

    public DataBaseM(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DataBaseM{" +
                "name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}
