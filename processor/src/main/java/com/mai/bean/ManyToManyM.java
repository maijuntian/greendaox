package com.mai.bean;

import com.mai.annotate.Cascade;

import java.util.List;

/**
 * Created by mai on 16/7/11.
 */
public class ManyToManyM extends BaseRelation{

    private TableM targetTable;

    private TableM midTable;

    private boolean isInverse;

    public TableM getMidTable() {
        return midTable;
    }

    public void setMidTable(TableM midTable) {
        this.midTable = midTable;
    }

    public boolean isInverse() {
        return isInverse;
    }

    public void setInverse(boolean inverse) {
        isInverse = inverse;
    }

}
