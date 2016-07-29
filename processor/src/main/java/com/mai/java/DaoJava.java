package com.mai.java;

import com.mai.bean.ColumnM;
import com.mai.bean.ManyToManyM;
import com.mai.bean.ToManyM;
import com.mai.bean.ToOneM;
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
    private boolean hasToOne;
    private boolean hasManyToMany;
    private boolean hasToMany;

    public DaoJava(String pkg, TableM table) {
        this.pkg = pkg;
        this.table = table;
        hasToOne = this.table.getToOnes() != null;
        hasManyToMany = this.table.getManyToManys() != null;
        hasToMany = this.table.getToManys() != null;
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
                "    public Map<String, Property> map = new HashMap<>();\n");
        if (hasToOne) { //存在多对一
            for (ToOneM manyToOne : table.getToOnes()) {
                builder.append("    public Map<" + table.getpK().getClazzName() + ", " + manyToOne.getTargetIdType() + "> " + manyToOne.getMapName() + " = new HashMap<>();\n");
            }
        }
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
        if (hasToOne) { //存在多对一
            for (ToOneM manyToOne : table.getToOnes()) {
                builder.append("        public static final Property " + manyToOne.getIdProperty() + " = new Property(" + manyToOne.getIndex() + ", " + manyToOne.getTargetIdType() + ".class, \"" + manyToOne.getTargetIdName() + "\", false, \"" + manyToOne.getTargetIdName() + "\");\n");
            }
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
        builder.append("                \"\\\"" + columnM.getName() + "\\\" " + parseTableType(columnM.getClazzName()) + (columnM.isNull() ? "" : " NOT NULL") + (!columnM.isUnique() ? "" : " UNIQUE"));

        if (hasToOne) {
            for (ToOneM toOneM : table.getToOnes())
                builder.append(",\" +\n" +
                        "                \"\\\"" + toOneM.getTargetIdName() + "\\\" " + parseTableType(toOneM.getTargetIdType()));
        }

        builder.append(");\");\n");

        if (table.getCreateIndexs() != null) {
            for (String createIndex : table.getCreateIndexs()) {
                builder.append("        db.execSQL(\"" + createIndex + "\");\n");
            }
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
        if (hasToOne) {
            for (ToOneM toOneM : table.getToOnes()) {
                builder.append("        if(entity.get" + toOneM.getUpperField() + "() != null) {\n" +
                        "            " + toOneM.getTargetIdType() + " " + toOneM.getTargetIdName() + " = entity.get" + toOneM.getUpperField() + "().get" + toOneM.getTargetIdUpperField() + "();\n" +
                        "            if(" + toOneM.getTargetIdName() + " != null){\n" +
                        "                stmt." + parseBindType(toOneM.getTargetIdType()) + "(" + (toOneM.getIndex() + 1) + ", " + toOneM.getTargetIdName() + ");\n" +
                        "            }\n" +
                        "        }\n");
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
        if (columnM.getClazzName().equals("Boolean")) {
            if (columnM.isboolean()) {
                if (field.startsWith("is")) {
                    return field;
                } else {
                    return "is" + columnM.getUpperField();
                }
            } else {
                if (field.length() > 2) {
                    char field3 = field.charAt(2);
                    if (Character.isUpperCase(field3)) { //第三个字母大写
                        return "is" + field.substring(2);
                    }
                }
            }

        }
        return "get" + columnM.getUpperField();
    }

    private String parseSetProperty(ColumnM columnM) {
        String field = columnM.getField();
        if (columnM.getClazzName().equals("Boolean") && field.startsWith("is")) {
            if (field.length() > 2) {
                char field3 = field.charAt(2);
                if (Character.isUpperCase(field3)) { //第三个字母大写
                    return "set" + field.substring(2);
                }
            }

        }
        return "set" + columnM.getUpperField();
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

        if (hasToOne) {
            for (ToOneM toOneM : table.getToOnes()) {
                builder.append("        if(!cursor.isNull(offset + " + toOneM.getIndex() + ")){\n" +
                        "           " + toOneM.getMapName() + ".put(entity.get" + table.getpK().getUpperField() + "(), " + parseCursorGetType(toOneM.getTargetIdType(), toOneM.getIndex()) + ");\n" +
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

        if (hasToOne) {
            for (ToOneM toOneM : table.getToOnes()) {
                builder.append("        if(!cursor.isNull(offset + " + toOneM.getIndex() + ")){\n" +
                        "           " + toOneM.getMapName() + ".put(entity.get" + table.getpK().getUpperField() + "(), " + parseCursorGetType(toOneM.getTargetIdType(), toOneM.getIndex()) + ");\n" +
                        "        }\n");
            }
        }
        builder.append("   }\n");
    }

    private void getLoadManyToOne(StringBuilder builder) {
        if (hasToOne) {
            for (ToOneM toOneM : table.getToOnes()) {
                builder.append("    public " + toOneM.getTargetType() + " load" + toOneM.getTargetName() + "(" + table.getpK().getClazzName() + " id){\n" +
                        "        " + toOneM.getTargetIdType() + " " + toOneM.getTargetIdName() + " = " + toOneM.getMapName() + ".get(id);\n" +
                        "        \n" +
                        "        if(" + toOneM.getTargetIdName() + " != null){\n" +
                        "            return session.load(" + toOneM.getTargetType() + ".class, " + toOneM.getTargetIdName() + ");\n" +
                        "        }\n" +
                        "        return null;\n" +
                        "    }\n");
            }
        }
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

    /**
     * 解析一对多
     */
    private void getLoadOneToMany(StringBuilder builder) {
        if (hasToMany) {
            for (ToManyM toManyM : table.getToManys()) {
                builder.append("    public java.util.List<" + toManyM.getTargetType() + "> load" + toManyM.getTargetName() + "s(" + table.getpK().getClazzName() + " id){\n" +
                        "        de.greenrobot.dao.query.QueryBuilder<" + toManyM.getTargetType() + "> qb = session.queryBuilder(" + toManyM.getTargetType() + ".class);\n" +
                        "        return qb.where(" + toManyM.getTargetName() + "Dao.Properties." + toManyM.getIdProperty() + ".eq(id)).list();\n" +
                        "    }\n");
            }
        }
    }

    private void getLoadManyToMany(StringBuilder builder) {
        if (hasManyToMany) {
            for (ManyToManyM manyToManyM : table.getManyToManys()) {
                builder.append("    public java.util.List<" + manyToManyM.getTargetTable().getType() + "> load" + manyToManyM.getTargetTable().getClazzName() + "s(" + table.getpK().getClazzName() + " id) {\n" +
                        "        String sql = \"SELECT t1.* FROM " + manyToManyM.getTargetTable().getName() + " t1, " + manyToManyM.getMidTable().getName() + " t2 WHERE t1." + table.getpK().getName() + " = t2." + manyToManyM.getTargetTable().getClazzName().toLowerCase() + "Id AND t2." + table.getClazzName().toLowerCase() + "Id=?\";\n" +
                        "        Cursor cursor = db.rawQuery(sql, new String[]{id + \"\"});\n" +
                        "        java.util.List<" + manyToManyM.getTargetTable().getType() + "> beans = new java.util.ArrayList<>();\n" +
                        "        try {\n" +
                        "            if (cursor.moveToFirst()) {\n" +
                        "                " + manyToManyM.getTargetTable().getClazzName() + "Dao beanDao = (" + manyToManyM.getTargetTable().getClazzName() + "Dao) session.getDao(" + manyToManyM.getTargetTable().getType() + ".class);\n" +
                        "                do {\n" +
                        "                    beans.add(beanDao.readEntity(cursor, 0));\n" +
                        "                } while (cursor.moveToNext());\n" +
                        "            }\n" +
                        "        } finally {\n" +
                        "            cursor.close();\n" +
                        "        }\n" +
                        "        return beans;\n" +
                        "    }\n");
            }
        }
    }

    private void getDeleteAll(StringBuilder buider) {

        buider.append("    @Override\n" +
                "    public void deleteAll() {\n" +
                "        super.deleteAll();\n" +
                "        cascadeDeleteAll();\n");
        if (hasManyToMany) {
            for (ManyToManyM manyToManyM : table.getManyToManys()) {
                buider.append("        db.execSQL(\"DELETE FROM " + manyToManyM.getMidTable().getName() + "\");\n");
            }
        }
        buider.append("    }\n\n");

        if (hasManyToMany) {
            buider.append("    @Override\n" +
                    "    protected void deleteByKeyInsideSynchronized(Long key, SQLiteStatement stmt) {\n" +
                    "        super.deleteByKeyInsideSynchronized(key, stmt);\n");
            for (ManyToManyM manyToManyM : table.getManyToManys()) {
                buider.append("        db.execSQL(\"DELETE FROM " + manyToManyM.getMidTable().getName() + " WHERE " + table.getClazzName().toLowerCase() + "Id=?\", new String[]{key+\"\"});\n");
            }
            buider.append("    }\n\n");
        }
    }

    private void getUpdateMidTable(StringBuilder builder) {
        if (hasManyToMany) {
            builder.append("    @Override\n" +
                    "    protected long executeInsert(" + table.getType() + " entity, SQLiteStatement stmt) {\n" +
                    "        long rowId = super.executeInsert(entity, stmt);\n" +
                    "        java.util.List<" + table.getType() + "> entities = new java.util.ArrayList<>();\n" +
                    "        entities.add(entity);\n" +
                    "        updateMidTable(entities);\n" +
                    "        return rowId;\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    protected void executeInsertInTx(SQLiteStatement stmt, Iterable<" + table.getType() + "> entities, boolean setPrimaryKey) {\n" +
                    "        super.executeInsertInTx(stmt, entities, setPrimaryKey);\n" +
                    "        updateMidTable(entities);\n" +
                    "    }\n" +
                    "\n" +
                    "    @Override\n" +
                    "    protected void updateInsideSynchronized(" + table.getType() + " entity, SQLiteStatement stmt, boolean lock) {\n" +
                    "        super.updateInsideSynchronized(entity, stmt, lock);\n" +
                    "        java.util.List<" + table.getType() + "> entities = new java.util.ArrayList<>();\n" +
                    "        entities.add(entity);\n" +
                    "        updateMidTable(entities);\n" +
                    "    }\n" +
                    "\n" +
                    "    private void updateMidTable(Iterable<" + table.getType() + "> entities){\n");
            for (ManyToManyM manyToManyM : table.getManyToManys()) {
                if (manyToManyM.isInverse()) { //是否维护关系
                    builder.append("        java.util.List<String> ids = new java.util.ArrayList<>();\n" +
                            "        java.util.List<" + manyToManyM.getMidTable().getType() + "> mid_s = new java.util.ArrayList<>();\n" +
                            "        " + manyToManyM.getMidTable().getClazzName() + "Dao mid_dao = (" + manyToManyM.getMidTable().getClazzName() + "Dao) session.getDao(" + manyToManyM.getMidTable().getType() + ".class);\n" +
                            "        StringBuilder builder = new StringBuilder(\"(\");\n" +
                            "        for (" + table.getType() + " entity : entities){\n" +
                            "            ids.add(entity.get" + table.getpK().getUpperField() + "() + \"\");\n" +
                            "            builder.append(\"?,\");\n" +
                            "            if(entity.get" + manyToManyM.getUpperField() + "() != null){\n" +
                            "                for (" + manyToManyM.getTargetTable().getType() + " target : entity.get" + manyToManyM.getUpperField() + "()){\n" +
                            "                    " + manyToManyM.getMidTable().getType() + " mid = new " + manyToManyM.getMidTable().getType() + "();\n" +
                            "                    if(target.get" + manyToManyM.getTargetTable().getpK().getUpperField() + "() == null){\n" +
                            "                        deleteInTx(entities);\n" +
                            "                        throw new IllegalStateException(\"Please insert " + manyToManyM.getTargetTable().getType() + " at first.\");\n" +
                            "                    }\n" +
                            "                    mid.set" + manyToManyM.getTargetTable().getClazzName() + "Id(target.get" + manyToManyM.getTargetTable().getpK().getUpperField() + "());\n" +
                            "                    mid.set" + table.getClazzName() + "Id(entity.get" + table.getpK().getUpperField() + "());\n" +
                            "                    mid_s.add(mid);\n" +
                            "                }\n" +
                            "            }\n" +
                            "        }\n" +
                            "        String delesql = builder.substring(0, builder.length() - 1) + \")\";\n" +
                            "\n" +
                            "        String[] idArrs = new String[ids.size()];\n" +
                            "        db.delete(\"" + manyToManyM.getMidTable().getName() + "\", \"" + table.getClazzName().toLowerCase() + "Id in \" + delesql, ids.toArray(idArrs));\n" +
                            "        mid_dao.insertInTx(mid_s);\n");
                }
            }
            builder.append("    }\n");
        }
    }

    private void getLoadLazy(StringBuilder builder) {
        if (!table.isLazy()) { //不需要懒加载
            builder.append("    /*** lazy load */\n" +
                    "    @Override\n" +
                    "    protected java.util.List<" + table.getType() + "> loadAllAndCloseCursor(Cursor cursor) {\n" +
                    "        java.util.List<" + table.getType() + "> entities = super.loadAllAndCloseCursor(cursor);\n" +
                    "        if (entities != null) {\n" +
                    "            loadLazy(entities);\n" +
                    "        }\n" +
                    "        return entities;\n" +
                    "    }\n" +
                    "\n" +
                    "    /*** lazy load */\n" +
                    "    @Override\n" +
                    "    protected " + table.getType() + " loadUniqueAndCloseCursor(Cursor cursor) {\n" +
                    "        " + table.getType() + " entity = super.loadUniqueAndCloseCursor(cursor);\n" +
                    "        java.util.List<" + table.getType() + "> entities = new java.util.ArrayList<>();\n" +
                    "        entities.add(entity);\n" +
                    "        loadLazy(entities);\n" +
                    "        return entity;\n" +
                    "    }\n" +
                    "\n" +
                    "    /*** lazy load */\n" +
                    "    private void loadLazy(Iterable<" + table.getType() + "> entities) {\n" +
                    "        for (" + table.getType() + " entity : entities) {\n");
            if (hasManyToMany) {
                for (ManyToManyM manyToManyM : table.getManyToManys()) {
                    builder.append("            entity.set" + manyToManyM.getUpperField() + "(load" + manyToManyM.getTargetTable().getClazzName() + "s(entity.get" + table.getpK().getUpperField() + "()));\n");
                }
            }
            if (hasToMany) {
                for (ToManyM toManyM : table.getToManys()) {
                    builder.append("            entity.set" + toManyM.getUpperField() + "(load" + toManyM.getTargetName() + "s(entity.get" + table.getpK().getUpperField() + "()));\n");
                }
            }
            if (hasToOne) {
                for (ToOneM toOneM : table.getToOnes()) {
                    builder.append("            entity.set" + toOneM.getUpperField() + "(load" + toOneM.getTargetName() + "(entity.get" + table.getpK().getUpperField() + "()));\n");
                }
            }
            builder.append("        }\n" +
                    "     }\n");
        }
    }

    /**
     * 级联插入，重写
     * public void insertInTx(Iterable<T> entities, boolean setPrimaryKey)
     * public long insert(T entity)
     *
     * @param builder
     */
    private void getCascadeInsert(StringBuilder builder) {

        if (table.isHasCascadeInsert()) {
            builder.append("     /*** cascade insert*/\n" +
                    "      @Override\n" +
                    "      public long insert(" + table.getType() + " entity) {\n" +
                    "            long rowId = super.insert(entity);\n" +
                    "            java.util.List<" + table.getType() + "> entities = new java.util.ArrayList<>();\n" +
                    "            entities.add(entity);\n" +
                    "            cascadeInsert(entities);\n" +
                    "            return rowId;\n" +
                    "        }\n" +
                    "\n" +
                    "        /*** cascade insert*/\n" +
                    "        @Override\n" +
                    "        public void insertInTx(Iterable<" + table.getType() + "> entities, boolean setPrimaryKey) {\n" +
                    "            super.insertInTx(entities, setPrimaryKey);\n" +
                    "            cascadeInsert(entities);\n" +
                    "        }\n" +
                    "\n" +
                    "        /*** cascade insert*/\n" +
                    "        private void cascadeInsert(Iterable<" + table.getType() + "> entities){\n" +
                    "            for (" + table.getType() + " entity : entities){\n");
            if (hasManyToMany) {
                for (ManyToManyM manyToManyM : table.getManyToManys()) {
                    if (manyToManyM.isCascadeInsert()) {
                        builder.append("                if(entity.get" + manyToManyM.getUpperField() + "() != null && entity.get" + manyToManyM.getUpperField() + "().size() > 0){\n" +
                                "                    session.insertInTx(entity.get" + manyToManyM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }
            if (hasToMany) {
                for (ToManyM toManyM : table.getToManys()) {
                    if (toManyM.isCascadeInsert()) {
                        builder.append("                if(entity.get" + toManyM.getUpperField() + "() != null && entity.get" + toManyM.getUpperField() + "().size() > 0){\n" +
                                "                    session.insertInTx(entity.get" + toManyM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }

            if (hasToOne) {
                for (ToOneM toOneM : table.getToOnes()) {
                    if (toOneM.isCascadeInsert()) {
                        builder.append("                if(entity.get" + toOneM.getUpperField() + "() != null) {\n" +
                                "                    session.insert(entity.get" + toOneM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }

            builder.append("            }\n" +
                    "        }\n");
        }
    }

    /**
     * 级联插入或替换，重写
     * public void insertOrReplaceInTx(Iterable<T> entities, boolean setPrimaryKey)
     * public long insertOrReplace(T entity)
     *
     * @param builder
     */
    private void getCascadeInsertOrReplace(StringBuilder builder) {
        if (table.isHasCascadeInsertOrReplace()) {
            builder.append("     /*** cascade insertOrReplace*/\n" +
                    "      @Override\n" +
                    "      public long insertOrReplace(" + table.getType() + " entity) {\n" +
                    "            long rowId = super.insertOrReplace(entity);\n" +
                    "            java.util.List<" + table.getType() + "> entities = new java.util.ArrayList<>();\n" +
                    "            entities.add(entity);\n" +
                    "            cascadeInsertOrReplace(entities);\n" +
                    "            return rowId;\n" +
                    "        }\n" +
                    "\n" +
                    "        /*** cascade insertOrReplace*/\n" +
                    "        @Override\n" +
                    "        public void insertOrReplaceInTx(Iterable<" + table.getType() + "> entities, boolean setPrimaryKey) {\n" +
                    "            super.insertOrReplaceInTx(entities, setPrimaryKey);\n" +
                    "            cascadeInsertOrReplace(entities);\n" +
                    "        }\n" +
                    "\n" +
                    "        /*** cascade insertOrReplace*/\n" +
                    "        private void cascadeInsertOrReplace(Iterable<" + table.getType() + "> entities){\n" +
                    "            for (" + table.getType() + " entity : entities){\n");
            if (hasManyToMany) {
                for (ManyToManyM manyToManyM : table.getManyToManys()) {
                    if (manyToManyM.isCascadeInsertOrReplace()) {
                        builder.append("                if(entity.get" + manyToManyM.getUpperField() + "() != null && entity.get" + manyToManyM.getUpperField() + "().size() > 0){\n" +
                                "                    session.insertOrReplaceInTx(entity.get" + manyToManyM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }
            if (hasToMany) {
                for (ToManyM toManyM : table.getToManys()) {
                    if (toManyM.isCascadeInsertOrReplace()) {
                        builder.append("                if(entity.get" + toManyM.getUpperField() + "() != null && entity.get" + toManyM.getUpperField() + "().size() > 0){\n" +
                                "                    session.insertOrReplaceInTx(entity.get" + toManyM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }

            if (hasToOne) {
                for (ToOneM toOneM : table.getToOnes()) {
                    if (toOneM.isCascadeInsertOrReplace()) {
                        builder.append("                if(entity.get" + toOneM.getUpperField() + "() != null) {\n" +
                                "                    session.insertOrReplace(entity.get" + toOneM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }

            builder.append("            }\n" +
                    "        }\n");
        }
    }

    /**
     * 级联更新，
     * 重写
     * public void updateInTx(Iterable<T> entities)
     * public void update(T entity)
     */
    private void getCascadeUpdate(StringBuilder builder) {
        if (table.isHasCascadeInsertOrReplace()) {
            builder.append("     /*** cascade update*/\n" +
                    "      @Override\n" +
                    "      public void update(" + table.getType() + " entity) {\n" +
                    "            super.update(entity);\n" +
                    "            java.util.List<" + table.getType() + "> entities = new java.util.ArrayList<>();\n" +
                    "            entities.add(entity);\n" +
                    "            cascadeUpdate(entities);\n" +
                    "        }\n" +
                    "\n" +
                    "        /*** cascade update*/\n" +
                    "        @Override\n" +
                    "        public void updateInTx(Iterable<" + table.getType() + "> entities) {\n" +
                    "            super.updateInTx(entities);\n" +
                    "            cascadeUpdate(entities);\n" +
                    "        }\n" +
                    "\n" +
                    "        /*** cascade update*/\n" +
                    "        private void cascadeUpdate(Iterable<" + table.getType() + "> entities){\n" +
                    "            for (" + table.getType() + " entity : entities){\n");
            if (hasManyToMany) {
                for (ManyToManyM manyToManyM : table.getManyToManys()) {
                    if (manyToManyM.isCascadeUpdate()) {
                        builder.append("                if(entity.get" + manyToManyM.getUpperField() + "() != null && entity.get" + manyToManyM.getUpperField() + "().size() > 0){\n" +
                                "                    session.updateInTx(entity.get" + manyToManyM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }
            if (hasToMany) {
                for (ToManyM toManyM : table.getToManys()) {
                    if (toManyM.isCascadeUpdate()) {
                        builder.append("                if(entity.get" + toManyM.getUpperField() + "() != null && entity.get" + toManyM.getUpperField() + "().size() > 0){\n" +
                                "                    session.updateInTx(entity.get" + toManyM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }

            if (hasToOne) {
                for (ToOneM toOneM : table.getToOnes()) {
                    if (toOneM.isCascadeUpdate()) {
                        builder.append("                if(entity.get" + toOneM.getUpperField() + "() != null) {\n" +
                                "                    session.update(entity.get" + toOneM.getUpperField() + "());\n" +
                                "                }\n");
                    }
                }
            }

            builder.append("            }\n" +
                    "        }\n");
        }
    }

    /**
     * 级联删除，
     * 新建方法
     * private void cascadeDeleteAll()
     * 重写
     * public void deleteByKey(K key)
     * proteced void deleteInTxInternal(Iterable<T> entities, Iterable<K> keys)
     *
     * @param builder
     */
    private void getCascadeDelete(StringBuilder builder) {

        builder.append("    /*** cascade delete*/\n" +
                "    public void cascadeDeleteAll(){\n");
        if (table.isHasCascadeDelete()) {
            if (hasManyToMany) {
                for (ManyToManyM manyToManyM : table.getManyToManys()) {
                    if (manyToManyM.isCascadeDelete()) {
                        builder.append("        db.execSQL(\"DELETE FROM \" + " + manyToManyM.getTargetTable().getClazzName() + "Dao.TABLENAME);\n");
                    }
                }
            }
            if (hasToMany) {
                for (ToManyM toManyM : table.getToManys()) {
                    if (toManyM.isCascadeDelete()) {
                        builder.append("        db.execSQL(\"DELETE FROM \" + " + toManyM.getTargetName() + "Dao.TABLENAME);\n");
                    }
                }
            }
            if (hasManyToMany) {
                for (ToOneM toOneM : table.getToOnes()) {
                    if (toOneM.isCascadeDelete()) {
                        builder.append("        db.execSQL(\"DELETE FROM \" + " + toOneM.getTargetName() + "Dao.TABLENAME);\n");
                    }
                }
            }
        }
        builder.append("    }\n");


        if (table.isHasCascadeInsertOrReplace()) {
            builder.append("     /*** cascade delete*/\n" +
                    "      @Override\n" +
                    "      public void deleteByKey(" + table.getpK().getClazzName() + " key) {\n" +
                    "            java.util.List<" + table.getpK().getClazzName() + "> keys = new java.util.ArrayList<>();\n" +
                    "            keys.add(key);\n" +
                    "            cascadeDelete(keys);\n" +
                    "            super.deleteByKey(key);\n" +
                    "        }\n" +
                    "\n" +
                    "        /*** cascade delete*/\n" +
                    "        @Override\n" +
                    "        protected void deleteInTxInternal(Iterable<" + table.getType() + "> entities, Iterable<" + table.getpK().getClazzName() + "> keys){\n" +
                    "            cascadeDelete(keys);\n" +
                    "            super.deleteInTxInternal(entities, keys);\n" +
                    "        }\n" +
                    "\n" +
                    "        /*** cascade delete*/\n" +
                    "        private void cascadeDelete(Iterable<Long> keys){\n" +
                    "            for (Long key : keys){\n");
            if (hasManyToMany) {
                for (ManyToManyM manyToManyM : table.getManyToManys()) {
                    if (manyToManyM.isCascadeDelete()) {
                        builder.append("                //ManyToMany\n" +
                                "                db.execSQL(\"DELETE FROM " + manyToManyM.getTargetTable().getName() + " WHERE " + manyToManyM.getTargetTable().getpK().getName() + " IN (SELECT t." + manyToManyM.getTargetTable().getClazzName().toLowerCase() + "Id FROM " + manyToManyM.getMidTable().getName() + " t WHERE t." + table.getClazzName().toLowerCase() + "Id =?)\", new String[]{key+\"\"});\n");
                    }
                }
            }
            if (hasToMany) {
                for (ToManyM toManyM : table.getToManys()) {
                    if (toManyM.isCascadeDelete()) {
                        builder.append("                //OneToMany\n" +
                                "                db.execSQL(\"DELETE FROM " + toManyM.getTargetTable().getName() + " WHERE " + table.getClazzName().toLowerCase() + "Id = ?\", new String[]{key + \"\"});\n");
                    }
                }
            }

            if (hasToOne) {
                for (ToOneM toOneM : table.getToOnes()) {
                    if (toOneM.isCascadeDelete()) {
                        builder.append("                //OneToOne, ManyToOne\n" +
                                "                db.execSQL(\"DELETE FROM " + toOneM.getTargetTable().getName() + " WHERE " + toOneM.getTargetTable().getpK().getName() + " IN (SELECT t." + toOneM.getTargetTable().getClazzName().toLowerCase() + "Id FROM " + table.getName() + " t WHERE t." + table.getpK().getName() + "=?)\", new String[]{key + \"\"});\n");
                    }
                }
            }

            builder.append("            }\n" +
                    "        }\n");
        }
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
        getLoadOneToMany(builder);
        getLoadManyToOne(builder);
        getLoadManyToMany(builder);
        getDeleteAll(builder);
        getUpdateMidTable(builder);
        getLoadLazy(builder);
        getCascadeInsert(builder);
        getCascadeInsertOrReplace(builder);
        getCascadeUpdate(builder);
        getCascadeDelete(builder);
        builder.append("}");
        return builder.toString();
    }
}
