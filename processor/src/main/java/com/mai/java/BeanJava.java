package com.mai.java;

import com.mai.bean.ColumnM;
import com.mai.bean.TableM;

/**
 * Created by mai on 16/7/14.
 */
public class BeanJava {

    private String pkg = null;
    private TableM midTable;

    public BeanJava(String pkg, TableM midTable) {
        this.pkg = pkg;
        this.midTable = midTable;
    }

    public void getField(StringBuilder builder) {
        for (ColumnM columnM : midTable.getColoums()) {
            builder.append("    private " + columnM.getClazzName() + " " + columnM.getField() + ";\n");
            builder.append("    public " + columnM.getClazzName() + " get" + columnM.getUpperField() + "() {\n" +
                    "        return " + columnM.getField() + ";\n" +
                    "    }\n" +
                    "\n" +
                    "    public void set" + columnM.getUpperField() + "(" + columnM.getClazzName() + " " + columnM.getField() + ") {\n" +
                    "        this." + columnM.getField() + " = " + columnM.getField() + ";\n" +
                    "    }\n");
        }
    }


    public String brewJava() {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + pkg + ";\n");
        builder.append("public class " + midTable.getClazzName() + " {\n");
        getField(builder);
        builder.append("}");
        return builder.toString();
    }
}
