package com.mai.bean;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;

/**
 * 表模型
 * Created by mai on 16/6/30.
 */
public class TableM {
    private String type;
    private String name;
    private String clazzName;
    private List<ColumnM> coloums;
    private Element element;
    private ColumnM pK;
    private String createIndex;

    public TableM(){}

    public void setElement(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ColumnM> getColoums() {
        return coloums;
    }

    public void setColoums(List<ColumnM> coloums) {
        this.coloums = coloums;
    }

    public void addColoum(ColumnM coloum){
        if(this.coloums == null)
            coloums = new ArrayList<ColumnM>();
        coloum.setIndex(this.coloums.size());
        this.coloums.add(coloum);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getClazzName() {
        return clazzName;
    }

    public ColumnM getpK() {
        return pK;
    }

    public void setpK(ColumnM pK) {
        this.pK = pK;
    }

    public String getCreateIndex() {
        return createIndex;
    }

    public void setCreateIndex(String createIndex) {
        this.createIndex = createIndex;
    }

    @Override
    public String toString() {
        return "TableM{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", clazzName='" + clazzName + '\'' +
                ", coloums=" + coloums +
                ", pK=" + pK +
                '}';
    }
}
