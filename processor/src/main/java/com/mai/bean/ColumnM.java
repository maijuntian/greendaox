package com.mai.bean;

/**
 * 列模型
 * Created by mai on 16/6/30.
 */
public class ColumnM {
    private String name; //列名
    private String type; //类型
    private String field; //属性名
    private String upperField;
    private String clazzName;
    private boolean isboolean;
    private boolean isNull = true;
    private int index;
    private boolean isPrimaryKey;
    private boolean isAuto;
    private boolean isUnique;

    public ColumnM() {
    }

    public boolean isboolean() {
        return isboolean;
    }

    public void setIsboolean(boolean isboolean) {
        this.isboolean = isboolean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public void setField(String field) {
        this.field = field;
        this.upperField = field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public String getField() {
        return field;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getClazzName() {
        return clazzName;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }

    public String getUpperField() {
        return upperField;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }
    public boolean isUnique() {
        return isUnique;
    }

    @Override
    public String toString() {
        return "ColumnM{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", field='" + field + '\'' +
                ", upperField='" + upperField + '\'' +
                ", clazzName='" + clazzName + '\'' +
                ", isNull=" + isNull +
                ", index=" + index +
                ", isPrimaryKey=" + isPrimaryKey +
                ", isAuto=" + isAuto +
                ", isUnique=" + isUnique +
                '}';
    }
}
