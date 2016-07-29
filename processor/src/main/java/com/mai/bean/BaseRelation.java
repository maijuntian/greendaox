package com.mai.bean;

import com.mai.annotate.Cascade;

import java.util.List;


/**
 * Created by mai on 16/7/20.
 */
public class BaseRelation {
    private String field;
    private String upperField;
    private boolean isLazy;

    private boolean cascadeInsert;

    private boolean cascadeInsertOrReplace;

    private boolean cascadeUpdate;

    private boolean cascadeDelete;


    private TableM targetTable;


    public String getField() {
        return field;
    }

    public String getUpperField() {
        return upperField;
    }

    public void setField(String field) {
        this.field = field;
        this.upperField = field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setCascades(List<Cascade> cascades) {
        for (Cascade cascade : cascades) {
            switch (cascade) {
                case INSERT:
                    cascadeInsert = true;
                    break;
                case INSERT_OR_REPLACE:
                    cascadeInsertOrReplace = true;
                    break;
                case UPDATE:
                    cascadeUpdate = true;
                    break;
                case DELETE:
                    cascadeDelete = true;
                    break;
            }
        }
    }

    public boolean isCascadeInsert() {
        return cascadeInsert;
    }

    public boolean isCascadeInsertOrReplace() {
        return cascadeInsertOrReplace;
    }

    public boolean isCascadeUpdate() {
        return cascadeUpdate;
    }

    public boolean isCascadeDelete() {
        return cascadeDelete;
    }

    public TableM getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(TableM targetTable) {
        this.targetTable = targetTable;
    }
}
