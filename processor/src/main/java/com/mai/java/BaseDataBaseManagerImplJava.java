package com.mai.java;

import com.mai.bean.DataBaseM;

/**
 * Created by mai on 16/7/2.
 */
public class BaseDataBaseManagerImplJava {

    private String pkg;

    private final String DEFAULT_PKG = "com.mai.xgreendao.base";

    private final String DATABASE_MANAGER_CLASS = DEFAULT_PKG + ".DBManagerImpl";

    private DataBaseM dataBaseM;

    public BaseDataBaseManagerImplJava(DataBaseM dataBaseM, String pkg) {
        this.dataBaseM = dataBaseM;
        this.pkg = pkg;
    }

    public String getDatabaseManagerClass() {
        return DATABASE_MANAGER_CLASS + this.dataBaseM.getKey();
    }

    public String brewJava() {
        return "package " + DEFAULT_PKG + ";\n" +
                "\n" +
                "\n" +
                "import android.content.Context;\n" +
                "\n" +
                "import de.greenrobot.dao.AbstractDaoSession;\n" +
                "import " + pkg + ".DaoMaster" + this.dataBaseM.getKey() + ";\n" +
                "import " + pkg + ".DaoSession" + this.dataBaseM.getKey() + ";\n" +
                "\n" +
                "public class DBManagerImpl"+ this.dataBaseM.getKey()+" implements DBManager.SessionInter{\n" +
                "\n" +
                "    @Override\n" +
                "    public AbstractDaoSession getDaoSession(Context context) {\n" +
                "        DaoMaster" + this.dataBaseM.getKey() + ".DevOpenHelper helper = new DaoMaster" + this.dataBaseM.getKey() + ".DevOpenHelper(context, \"" + dataBaseM.getName() + "\", null);\n" +
                "        DaoSession" + this.dataBaseM.getKey() + " mDaoSession = new DaoMaster" + this.dataBaseM.getKey() + "(helper.getWritableDatabase()).newSession();\n" +
                "        return mDaoSession;\n" +
                "    }\n" +
                "}";
    }
}
