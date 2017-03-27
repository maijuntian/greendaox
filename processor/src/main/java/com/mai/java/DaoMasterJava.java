package com.mai.java;

import com.mai.bean.DataBaseM;
import com.mai.bean.TableM;

import java.util.List;

/**
 * Created by mai on 16/7/1.
 */
public class DaoMasterJava {
    private List<TableM> tables;
    private String pkg;
    private DataBaseM dataBaseM;

    public DaoMasterJava(List<TableM> tables, String pkg, DataBaseM dataBaseM) {
        this.tables = tables;
        this.pkg = pkg;
        this.dataBaseM = dataBaseM;
    }

    private void getField(StringBuilder builder) {
        builder.append("\n" +
                "    public static final int SCHEMA_VERSION = " + dataBaseM.getVersion() + ";\n");
    }

    private void getImport(StringBuilder builder) {
        builder.append("package " + pkg + ";\n" +
                "\n" +
                "import android.content.Context;\n" +
                "import android.database.sqlite.SQLiteDatabase;\n" +
                "import android.database.sqlite.SQLiteDatabase.CursorFactory;\n" +
                "import android.database.sqlite.SQLiteOpenHelper;\n" +
                "import android.util.Log;\n" +
                "import de.greenrobot.dao.AbstractDaoMaster;\n" +
                "import de.greenrobot.dao.identityscope.IdentityScopeType;\n\n");
    }

    private void getGreateTable(StringBuilder builder) {
        builder.append("    /** Creates underlying database table using DAOs. */\n" +
                "    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {\n");
        for (TableM tableM : tables) {
            builder.append("        " + tableM.getClazzName() + "Dao.createTable(db, ifNotExists);\n");
        }

        builder.append("    }\n");
    }

    private void getDropTable(StringBuilder builder) {
        builder.append("    /** Drops underlying database table using DAOs. */\n" +
                "    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {\n");
        for (TableM tableM : tables) {
            builder.append("        " + tableM.getClazzName() + "Dao.dropTable(db, ifExists);\n");
        }
        builder.append("    }\n");
    }

    private void getHelper(StringBuilder builder) {
        builder.append("    public static abstract class OpenHelper extends SQLiteOpenHelper {\n" +
                "\n" +
                "        public OpenHelper(Context context, String name, CursorFactory factory) {\n" +
                "            super(context, name, factory, SCHEMA_VERSION);\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        public void onCreate(SQLiteDatabase db) {\n" +
                "            Log.d(\"xgreenDAO\", \"Creating tables for schema version \" + SCHEMA_VERSION);\n" +
                "            createAllTables(db, false);\n" +
                "        }\n" +
                "    }\n" +
                "    \n" +
                "    /** WARNING: Drops all table on Upgrade! Use only during development. */\n" +
                "    public static class DevOpenHelper extends OpenHelper {\n" +
                "        public DevOpenHelper(Context context, String name, CursorFactory factory) {\n" +
                "            super(context, name, factory);\n" +
                "        }\n" +
                "\n" +
                "        @Override\n" +
                "        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {\n" +
                "            Log.d(\"xgreenDAO\", \"Upgrading schema from version \" + oldVersion + \" to \" + newVersion + \" by dropping all tables\");\n" +
                "            dropAllTables(db, true);\n" +
                "            onCreate(db);\n" +
                "        }\n" +
                "    }\n");
    }

    private void getConstructor(StringBuilder builder) {
        builder.append("    public DaoMaster" + getDbKey() + "(SQLiteDatabase db) {\n" +
                "        super(db, SCHEMA_VERSION);\n");

        for (TableM tableM : tables) {
            builder.append("        registerDaoClass(" + tableM.getClazzName() + "Dao.class);\n");
        }

        builder.append("    }\n");
    }

    private void getSession(StringBuilder builder) {
        builder.append("    public DaoSession" + getDbKey() + " newSession() {\n" +
                "        return new DaoSession" + getDbKey() + "(db, IdentityScopeType.Session, daoConfigMap);\n" +
                "    }\n" +
                "    \n" +
                "    public DaoSession"+ getDbKey() + " newSession(IdentityScopeType type) {\n" +
                "        return new DaoSession" + getDbKey() + "(db, type, daoConfigMap);\n" +
                "    }\n");
    }

    private int getDbKey() {
        return tables.get(0).getDbKey();
    }

    public String brewJava() {
        StringBuilder builder = new StringBuilder();
        getImport(builder);
        builder.append("public class DaoMaster" + getDbKey() + " extends AbstractDaoMaster {\n");
        getField(builder);
        getGreateTable(builder);
        getDropTable(builder);
        getHelper(builder);
        getConstructor(builder);
        getSession(builder);
        builder.append("}\n");
        return builder.toString();
    }
}
