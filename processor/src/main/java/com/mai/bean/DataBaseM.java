package com.mai.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库模型
 * Created by mai on 16/6/30.
 */
public class DataBaseM {
    private int key;
    private String name;
    private int version;
    private List<TableM> tableMs;

    public DataBaseM() {
    }

    public DataBaseM(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public DataBaseM(int key, String name, int version) {
        this.key = key;
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

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public List<TableM> getTableMs() {
        return tableMs;
    }

    public void setTableMs(List<TableM> tableMs) {
        this.tableMs = tableMs;
    }

    public void addTable(TableM tableM){
        if(tableMs == null)
            tableMs = new ArrayList<>();
        tableMs.add(tableM);
    }

    @Override
    public String toString() {
        return "DataBaseM{" +
                "key=" + key +
                ", name='" + name + '\'' +
                ", version=" + version +
                '}';
    }
}
