package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.Table;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by mai on 16/7/24.
 */
@Table(name = "all_type" /**可以指定名字*/ , createIndex = {"CREATE UNIQUE INDEX IDX_TESTLONG_TESTINT ON all_type(testLong, testInt);"}/** 可选，添加索引*/)
public class AllType {


    @Id(autoIncrement = true)
    private Long id;
    @Column
    private Long testLong;
    @Column/*(unique = true *//**设置唯一*//*)*/
    private int testInt;
    @Column (name = "date" /**可以指定名字*/)
    private java.util.Date testDate;
    @Column
    private Boolean testBoolean;
    @Column
    private byte[] testByteArray;
    @Column
    private Byte testByte;
    @Column
    private Double testDouble;
    @Column
    private Float testFloat;
    @Column
    private Short testShort;
    @Column
    private String testString;

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public Boolean getTestBoolean() {
        return testBoolean;
    }

    public void setTestBoolean(Boolean testBoolean) {
        this.testBoolean = testBoolean;
    }

    public Long getTestLong() {
        return testLong;
    }

    public void setTestLong(Long testLong) {
        this.testLong = testLong;
    }

    public byte[] getTestByteArray() {
        return testByteArray;
    }

    public void setTestByteArray(byte[] testByteArray) {
        this.testByteArray = testByteArray;
    }

    public Byte getTestByte() {
        return testByte;
    }

    public void setTestByte(Byte testByte) {
        this.testByte = testByte;
    }

    public Double getTestDouble() {
        return testDouble;
    }

    public void setTestDouble(Double testDouble) {
        this.testDouble = testDouble;
    }

    public Float getTestFloat() {
        return testFloat;
    }

    public void setTestFloat(Float testFloat) {
        this.testFloat = testFloat;
    }

    public Short getTestShort() {
        return testShort;
    }

    public void setTestShort(Short testShort) {
        this.testShort = testShort;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AllType{" +
                "testInt=" + testInt +
                ", testDate=" + testDate +
                ", testBoolean=" + testBoolean +
                ", testLong=" + testLong +
                ", testByteArray=" + Arrays.toString(testByteArray) +
                ", testByte=" + testByte +
                ", testDouble=" + testDouble +
                ", testFloat=" + testFloat +
                ", testShort=" + testShort +
                ", testString='" + testString + '\'' +
                '}';
    }
}
