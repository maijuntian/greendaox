package com.mai.bean;

import com.mai.annotate.Cascade;

import java.util.List;

/**
 * Created by mai on 16/7/11.
 */
public class ToManyM extends BaseRelation{
    private String targetType;
    private String targetName;
    private String idProperty;

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
        this.targetName = targetType.substring(targetType.lastIndexOf(".") + 1);
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }


    @Override
    public String toString() {
        return "ToManyM{" +
                "targetType='" + targetType + '\'' +
                ", targetName='" + targetName + '\'' +
                ", idProperty='" + idProperty + '\'' +
                '}';
    }

}
