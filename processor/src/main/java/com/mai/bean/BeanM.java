package com.mai.bean;

import java.util.List;

/**
 * Created by mai on 16/7/14.
 */
public class BeanM {

    private String pk;
    private String className;
    private List<String> types;
    private List<String> fileds;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getFileds() {
        return fileds;
    }

    public void setFileds(List<String> fileds) {
        this.fileds = fileds;
    }

    @Override
    public String toString() {
        return "BeanM{" +
                "types=" + types +
                ", fileds=" + fileds +
                '}';
    }
}
