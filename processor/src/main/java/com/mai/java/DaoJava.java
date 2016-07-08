package com.mai.java;

import com.mai.bean.ColumnM;
import com.mai.bean.TableM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mai on 16/7/1.
 */
public class DaoJava {

    static Map<String, String> tableTypes = new HashMap<>();

    static Map<String, String> bindTypes = new HashMap<>();

    static Map<String, String> getCursorTypes = new HashMap<>();

    static {
        tableTypes.put("Long", "INTEGER");
        tableTypes.put("Integer", "INTEGER");
        tableTypes.put("Boolean", "INTEGER");
        tableTypes.put("Byte", "INTEGER");
        tableTypes.put("Short", "INTEGER");
        tableTypes.put("Float", "REAL");
        tableTypes.put("Double", "REAL");
        tableTypes.put("byte[]", "BLOB");
        tableTypes.put("Date", "INTEGER");
        tableTypes.put("String", "TEXT");

        bindTypes.put("Long", "bindLong");
        bindTypes.put("Integer", "bindLong");
        bindTypes.put("Boolean", "bindLong");
        bindTypes.put("Byte", "bindLong");
        bindTypes.put("Short", "bindLong");
        bindTypes.put("Float", "bindDouble");
        bindTypes.put("Double", "bindDouble");
        bindTypes.put("byte[]", "bindBlob");
        bindTypes.put("Date", "bindLong");
        bindTypes.put("String", "bindString");

        getCursorTypes.put("Long", "getLong");
        getCursorTypes.put("Integer", "getInt");
        getCursorTypes.put("Boolean", "getShort");
        getCursorTypes.put("Byte", "getShort");
        getCursorTypes.put("Short", "getShort");
        getCursorTypes.put("Float", "getFloat");
        getCursorTypes.put("Double", "getDouble");
        getCursorTypes.put("byte[]", "getBlob");
        getCursorTypes.put("Date", "getLong");
        getCursorTypes.put("String", "getString");
    }

    private String pkg;
    private TableM table;

    public DaoJava(String pkg, TableM table) {
        this.pkg = pkg;
        this.table = table;
    }

    public String getDaoName() {
        return table.getClazzName() + "Dao";
    }

    private void getImport(StringBuilder builder) {
        builder.append("package " + pkg + ";\n" +
                "\n" +
                "import android.database.Cursor;\n" +
                "import android.database.sqlite.SQLiteDatabase;\n" +
                "import android.database.sqlite.SQLiteStatement;\n" +
                "\n" +
                "import de.greenrobot.dao.AbstractDao;\n" +
                "import de.greenrobot.dao.Property;\n" +
                "import de.greenrobot.dao.internal.DaoConfig;\n" +
                "\n" +
                "import java.util.Date;\n" +
                "import java.util.HashMap;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "import " + table.getType() + ";\n" +
                "\n");
    }

    private void getField(StringBuilder builder) {
        builder.append("    public static final String TABLENAME = \"" + table.getName() + "\";\n" +
                "\n" +
                "    public static Map<String, Property> map = new HashMap<>();\n");
    }

    private void getInitMap(StringBuilder builder) {
        builder.append("    private void initMap(){\n");
        for (ColumnM columnM : table.getColoums()) {
            builder.append("        map.put(\"" + columnM.getField() + "\", Properties." + columnM.getUpperField() + ");\n");
        }
        builder.append("    }\n");
    }

    private void getProperty(StringBuilder builder) {

        builder.append("    public static class Properties {\n");
        List<ColumnM> coloums = table.getColoums();
        for (ColumnM columnM : coloums) {
            builder.append("        public static final Property " + columnM.getUpperField() + " = new Property(" + columnM.getIndex() + ", " + columnM.getClazzName() + ".class, \"" + columnM.getField() + "\", " + columnM.isPrimaryKey() + ", \"" + columnM.getName() + "\");\n");
        }
        builder.append("        public Properties() {\n" +
                "        }\n" +
                "    }\n");
    }

    private void getConstructor(StringBuilder builder) {
        builder.append("    public " + getDaoName() + "(DaoConfig config) {\n" +
                "        super(config);\n" +
                "        initMap();\n" +
                "    }\n" +
                "    \n" +
                "    public " + getDaoName() + "(DaoConfig config, DaoSession daoSession) {\n" +
                "        super(config, daoSession);\n" +
                "        initMap();\n" +
                "    }"
        );
    }

    private String parseTableType(String type) {
        return tableTypes.get(type);
    }

    private void getCreateTable(StringBuilder builder) {
        builder.append("    /** Creates the underlying database table. */\n" +
                "    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {\n" +
                "        String constraint = ifNotExists? \"IF NOT EXISTS \": \"\";\n" +
                "        db.execSQL(\"CREATE TABLE \" + constraint + \"\\\"" + table.getName() + "\\\" (\" + //\n");
        for (int i = 0, size = table.getColoums().size(); i < size - 1; i++) {
            ColumnM columnM = table.getColoums().get(i);
            if (columnM.isPrimaryKey()) {
                builder.append("                \"\\\"" + columnM.getName() + "\\\" " + parseTableType(columnM.getClazzName()) + " PRIMARY KEY" + (columnM.isAuto() ? " AUTOINCREMENT" : "") + (columnM.isNull() ? "" : " NOT NULL") + ",\" + \n");
            } else {
                builder.append("                \"\\\"" + columnM.getName() + "\\\" " + parseTableType(columnM.getClazzName()) + (columnM.isNull() ? "" : " NOT NULL") + (!columnM.isUnique() ? "" : " UNIQUE") + ",\" + \n");
            }
        }
        ColumnM columnM = table.getColoums().get(table.getColoums().size() - 1);
        builder.append("                \"\\\"" + columnM.getName() + "\\\" " + parseTableType(columnM.getClazzName()) + (columnM.isNull() ? "" : " NOT NULL") + (!columnM.isUnique() ? "" : " UNIQUE") + ");\");\n");

        if (!"".equals(table.getCreateIndex())) {
            builder.append("db.execSQL(\"" + table.getCreateIndex() + "\");\n");
        }
        builder.append("   }\n");

    }

    private void getDropTable(StringBuilder builder) {
        builder.append("    /** Drops the underlying database table. */\n" +
                "    public static void dropTable(SQLiteDatabase db, boolean ifExists) {\n" +
                "        String sql = \"DROP TABLE \" + (ifExists ? \"IF EXISTS \" : \"\") + \"\\\"" + table.getName() + "\\\"\";\n" +
                "        db.execSQL(sql);\n" +
                "    }");
    }

    private void getBindValue(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    protected void bindValues(SQLiteStatement stmt, " + table.getClazzName() + " entity) {\n" +
                "        stmt.clearBindings();\n");
        for (ColumnM columnM : table.getColoums()) {
            builder.append("        " + columnM.getClazzName() + " " + columnM.getField() + " = entity." + parseGetProperty(columnM) + "();\n");
            if (columnM.isPrimaryKey() || columnM.isNull()) {
                builder.append("        if (" + columnM.getField() + " != null) {\n" +
                        "            stmt." + parseBindType(columnM.getClazzName()) + "(" + (columnM.getIndex() + 1) + ", " + columnM.getField() + (columnM.getClazzName().equals("Date") ? ".getTime()" : "") + (columnM.getClazzName().equals("Boolean") ? " ? 1L: 0L" : "") + ");\n" +
                        "        }\n");
            } else {
                builder.append("        stmt." + parseBindType(columnM.getClazzName()) + "(" + (columnM.getIndex() + 1) + ", " + columnM.getField() + (columnM.getClazzName().equals("Date") ? ".getTime()" : "") + (columnM.getClazzName().equals("Boolean") ? " ? 1L: 0L" : "") + ");\n");
            }
        }
        builder.append("    }\n");
    }

    private String parseBindType(String type) {
        return bindTypes.get(type);
    }

    private String parseCursorGetType(String type, int index) {
        String cursorType = "cursor." + getCursorTypes.get(type) + "(offset + " + index + ")";

        if (type.equals("Boolean")) {
            cursorType += " != 0";
        } else if (type.equals("Date")) {
            cursorType = "new Date(" + cursorType + ")";
        } else if (type.equals("Byte")) {
            cursorType = "(byte)" + cursorType;
        }
        return cursorType;
    }

    private String parseGetProperty(ColumnM columnM) {
        String field = columnM.getField();
        if (columnM.getClazzName().equals("Boolean") && field.startsWith("is")) {
            if (field.length() > 2) {
                char field3 = field.charAt(2);
                if (Character.isUpperCase(field3)) { //第三个字母大写
                    return "is" + field.substring(2);
                }
            }

        }
        return "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    private String parseSetProperty(ColumnM columnM) {
        String field = columnM.getField();
        if ((columnM.getClazzName().equals("Boolean") || columnM.getClazzName().equals("boolean")) && field.startsWith("is")) {
            if (field.length() > 2) {
                char field3 = field.charAt(2);
                if (Character.isUpperCase(field3)) { //第三个字母大写
                    return "set" + field.substring(2);
                }
            }

        }
        return "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
    }


    private void getReadKey(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    public " + table.getpK().getClazzName() + " readKey(Cursor cursor, int offset) {\n" +
                "        return cursor.isNull(offset + 0) ? null : " + parseCursorGetType(table.getpK().getClazzName(), 0) + ";\n" +
                "    }\n");
    }


    private void getReadEntity(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    public " + table.getClazzName() + " readEntity(Cursor cursor, int offset) {\n" +
                "        " + table.getClazzName() + " entity = new " + table.getClazzName() + "();\n");
        for (ColumnM columnM : table.getColoums()) {
            if (!columnM.isNull() || columnM.isPrimaryKey()) {
                builder.append("         entity." + parseSetProperty(columnM) + "(" + parseCursorGetType(columnM.getClazzName(), columnM.getIndex()) + ");\n");
            } else {
                builder.append("        if(!cursor.isNull(offset + " + columnM.getIndex() + ")){\n" +
                        "            entity." + parseSetProperty(columnM) + "(" + parseCursorGetType(columnM.getClazzName(), columnM.getIndex()) + ");\n" +
                        "        }\n");
            }

        }
        builder.append("        return entity;\n" +
                "   }\n");
    }

    private void getReadEntity2(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    public void readEntity(Cursor cursor, " + table.getClazzName() + " entity, int offset) {\n");
        for (ColumnM columnM : table.getColoums()) {
            if (!columnM.isNull() || columnM.isPrimaryKey()) {
                builder.append("        entity." + parseSetProperty(columnM) + "(" + parseCursorGetType(columnM.getClazzName(), columnM.getIndex()) + ");\n");
            } else {
                builder.append("        if(!cursor.isNull(offset + " + columnM.getIndex() + ")){\n" +
                        "            entity." + parseSetProperty(columnM) + "(" + parseCursorGetType(columnM.getClazzName(), columnM.getIndex()) + ");\n" +
                        "        }\n");
            }

        }
        builder.append("   }\n");
    }

    private void getUpdateKeyAfterInsert(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    protected " + table.getpK().getClazzName() + " updateKeyAfterInsert(" + table.getClazzName() + " entity, long rowId) {\n");
        if (table.getpK().isAuto()) {
            builder.append("        entity." + parseSetProperty(table.getpK()) + "(rowId);\n" +
                    "        return rowId;\n");
        } else {
            builder.append("        return entity." + parseGetProperty(table.getpK()) + "();\n");
        }
        builder.append("    }\n");

    }

    private void getKey(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    public " + table.getpK().getClazzName() + " getKey(" + table.getClazzName() + " entity) {\n" +
                "        if(entity != null) {\n" +
                "            return entity." + parseGetProperty(table.getpK()) + "();\n" +
                "        } else {\n" +
                "            return null;\n" +
                "        }\n" +
                "    }\n");
    }

    private void getIsEntityUpdateable(StringBuilder builder) {
        builder.append("    @Override    \n" +
                "    protected boolean isEntityUpdateable() {\n" +
                "        return true;\n" +
                "    }\n");
    }

    private void get(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    public Property get(String key) {\n" +
                "        return map.get(key);\n" +
                "    }\n");
    }

    public String brewJava() {
        if (table.getpK() == null) {
            throw new IllegalStateException(table.getClazzName() + " miss primary key");
        }
        StringBuilder builder = new StringBuilder();
        getImport(builder);
        builder.append("public class " + getDaoName() + " extends AbstractDao<" + table.getClazzName() + ", " + table.getpK().getClazzName() + "> {\n");
        getField(builder);
        getProperty(builder);
        getInitMap(builder);
        getConstructor(builder);
        getCreateTable(builder);
        getDropTable(builder);
        getBindValue(builder);
        getReadKey(builder);
        getReadEntity(builder);
        getReadEntity2(builder);
        getUpdateKeyAfterInsert(builder);
        getKey(builder);
        getIsEntityUpdateable(builder);
        get(builder);
        builder.append("}");
        return builder.toString();
    }
}
