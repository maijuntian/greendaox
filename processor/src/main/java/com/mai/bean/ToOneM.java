package com.mai.bean;

import com.mai.annotate.Cascade;

import java.util.List;

/**
 * Created by mai on 16/7/11.
 */
public class ToOneM extends BaseRelation{
    private String targetName;
    private String targetType;
    private String targetIdType;
    private String targetIdName;
    private String targetIdUpperField;
    private String idProperty;
    private int index;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
        this.targetName = targetType.substring(targetType.lastIndexOf(".") + 1);
        this.targetIdName = targetName.toLowerCase() + "Id";
    }


    public String getTargetIdType() {
        return targetIdType;
    }

    public void setTargetIdType(String targetIdType) {
        this.targetIdType = targetIdType;
    }

    public String getTargetIdName() {
        return targetIdName;
    }

    public void setTargetIdName(String targetIdName) {
        this.targetIdName = targetIdName;
    }

    public String getTargetIdUpperField() {
        return targetIdUpperField;
    }

    public void setTargetIdUpperField(String targetIdUpperField) {
        this.targetIdUpperField = targetIdUpperField;
    }


    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getMapName() {
        return "map" + targetName;
    }

    @Override
    public String toString() {
        return "ToOneM{" +
                "targetName='" + targetName + '\'' +
                ", targetType='" + targetType + '\'' +
                ", targetIdType='" + targetIdType + '\'' +
                ", targetIdName='" + targetIdName + '\'' +
                ", targetIdUpperField='" + targetIdUpperField + '\'' +
                ", idProperty='" + idProperty + '\'' +
                ", index=" + index +
                '}';
    }
}
