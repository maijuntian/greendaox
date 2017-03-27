package com.mai.java;

import com.mai.bean.TableM;

import java.util.List;

/**
 * Created by mai on 16/6/29.
 */
public class DaoSessionJava {
    List<TableM> tables;

    String pkg;

    public DaoSessionJava(String pkg, List<TableM> tables) {
        this.pkg = pkg;
        this.tables = tables;
    }

    public String getImport(StringBuilder builder) {
        builder.append("package " + pkg + ";\n" +
                "\n" +
                "import android.database.sqlite.SQLiteDatabase;\n" +
                "import java.util.Map;\n" +
                "import de.greenrobot.dao.AbstractDao;\n" +
                "import de.greenrobot.dao.AbstractDaoSession;\n" +
                "import de.greenrobot.dao.identityscope.IdentityScopeType;\n" +
                "import de.greenrobot.dao.internal.DaoConfig;\n");
        for (TableM tableM : tables) {
            builder.append("import " + tableM.getType() + ";\n");
        }
        return builder.toString();
    }

    private void getFields(StringBuilder builder) {
        for (TableM tableM : tables) {
            builder.append("   private final DaoConfig " + tableM.getClazzName().toLowerCase() + "DaoConfig;\n" +
                    "   private final " + tableM.getClazzName() + "Dao " + tableM.getClazzName().toLowerCase() + "Dao;\n");
        }
    }

    private void getConstructor(StringBuilder builder) {
        builder.append("   public DaoSession" + getDbKey() + "(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>\n" +
                "            daoConfigMap) {\n" +
                "        super(db);\n" +
                "\n");
        for (TableM tableM : tables) {
            builder.append("        " + tableM.getClazzName().toLowerCase() + "DaoConfig = daoConfigMap.get(" + tableM.getClazzName() + "Dao.class).clone();\n" +
                    "        " + tableM.getClazzName().toLowerCase() + "DaoConfig.initIdentityScope(type);\n" +
                    "        " + tableM.getClazzName().toLowerCase() + "Dao = new " + tableM.getClazzName() + "Dao(" + tableM.getClazzName().toLowerCase() + "DaoConfig, this);\n" +
                    "        registerDao(" + tableM.getClazzName() + ".class, " + tableM.getClazzName().toLowerCase() + "Dao);\n");
        }
        builder.append("   }\n");
    }

    private void getClear(StringBuilder builder) {
        builder.append("    public void clear() {\n");
        for (TableM tableM : tables) {
            builder.append("        " + tableM.getClazzName().toLowerCase() + "DaoConfig.getIdentityScope().clear();\n");
        }
        builder.append("    }\n");
    }

    private void getDao(StringBuilder builder) {
        for (TableM tableM : tables) {
            builder.append("    public " + tableM.getClazzName() + "Dao get" + tableM.getClazzName() + "Dao() {\n" +
                    "        return " + tableM.getClazzName().toLowerCase() + "Dao;\n" +
                    "    }\n");
        }
    }

    private int getDbKey() {
        return tables.get(0).getDbKey();
    }

    public String brewJave() {
        StringBuilder builder = new StringBuilder();
        getImport(builder);
        builder.append("public class DaoSession" + getDbKey() + " extends AbstractDaoSession {\n");
        getFields(builder);
        getConstructor(builder);
        getClear(builder);
        getDao(builder);
        builder.append("}\n");

        return builder.toString();
    }
}
