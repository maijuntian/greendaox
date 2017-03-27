package com.mai.bean;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;

/**
 * 表模型
 * Created by mai on 16/6/30.
 */
public class TableM {
    private int dbKey;
    private String type;
    private String name;
    private String clazzName;
    private List<ColumnM> coloums;
    private Element element;
    private ColumnM pK;
    private List<String> createIndexs;
    private boolean isMidTable;

    private boolean hasCascadeInsert;
    private boolean hasCascadeUpdate;
    private boolean hasCascadeInsertOrReplace;
    private boolean hasCascadeDelete;

    private boolean isLazy = true;

    List<ManyToManyM> manyToManys;
    List<ToOneM> toOnes;
    List<ToManyM> toManys;

    public void addManyToMany(ManyToManyM manyToManyM) {
        if (manyToManys == null)
            manyToManys = new ArrayList<>();
        manyToManys.add(manyToManyM);

        parseRelation(manyToManyM);
    }

    public void addToOne(ToOneM toOneM) {
        if (toOnes == null)
            toOnes = new ArrayList<>();
        toOneM.setIndex(toOnes.size() + coloums.size());
        toOnes.add(toOneM);

        parseRelation(toOneM);
    }

    public void addToMany(ToManyM toManyM) {
        if (toManys == null)
            toManys = new ArrayList<>();
        toManys.add(toManyM);

        parseRelation(toManyM);
    }

    private void parseRelation(BaseRelation relation){
        if(!relation.isLazy())
            isLazy = false;

        if(relation.isCascadeInsert())
            hasCascadeInsert = relation.isCascadeInsert();
        if(relation.isCascadeInsertOrReplace())
            hasCascadeInsertOrReplace = relation.isCascadeInsertOrReplace();
        if(relation.isCascadeUpdate())
            hasCascadeUpdate = relation.isCascadeUpdate();
        if(relation.isCascadeDelete())
            hasCascadeDelete = relation.isCascadeDelete();
    }

    public void addOneToOneIndex(TableM targetTableM) {
        String index = "CREATE UNIQUE INDEX IF NOT EXISTS IDX_" + targetTableM.getClazzName().toUpperCase() + " ON " + name + "(" + targetTableM.getClazzName().toLowerCase() + "Id);";
        addCreateIndexs(index);
    }

    public TableM() {
    }

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

    public void addColoum(ColumnM coloum) {
        if (this.coloums == null)
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

    public int getDbKey() {
        return dbKey;
    }

    public void setDbKey(int dbKey) {
        this.dbKey = dbKey;
    }

    public List<String> getCreateIndexs() {
        return createIndexs;
    }

    public void addCreateIndexs(String... createIndex) {
        if (createIndexs == null)
            createIndexs = new ArrayList<>();
        createIndexs.addAll(Arrays.asList(createIndex));
    }

    public List<ManyToManyM> getManyToManys() {
        return manyToManys;
    }


    public List<ToOneM> getToOnes() {
        return toOnes;
    }


    public List<ToManyM> getToManys() {
        return toManys;
    }


    public void setMidTable(boolean midTable) {
        isMidTable = midTable;
    }

    public boolean isMidTable() {
        return isMidTable;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    public boolean isHasCascadeInsert() {
        return hasCascadeInsert;
    }

    public boolean isHasCascadeUpdate() {
        return hasCascadeUpdate;
    }

    public boolean isHasCascadeInsertOrReplace() {
        return hasCascadeInsertOrReplace;
    }

    public boolean isHasCascadeDelete() {
        return hasCascadeDelete;
    }

    @Override
    public String toString() {
        return "TableM{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", clazzName='" + clazzName + '\'' +
                ", coloums=" + coloums +
                ", element=" + element +
                ", pK=" + pK +
                ", createIndexs=" + createIndexs +
                ", isMidTable=" + isMidTable +
                ", isLazy=" + isLazy +
                ", manyToManys=" + manyToManys +
                ", toOnes=" + toOnes +
                ", toManys=" + toManys +
                ", dbKey=" + dbKey +
                '}';
    }
}
