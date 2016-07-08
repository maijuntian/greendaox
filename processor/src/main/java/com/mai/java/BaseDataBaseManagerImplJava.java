package com.mai.java;

import com.mai.bean.DataBaseM;

/**
 * Created by mai on 16/7/2.
 */
public class BaseDataBaseManagerImplJava {

    private String pkg;

    private final String DEFAULT_PKG = "com.mai.xgreendao.base";

    private final String DATABASE_MANAGER_CLASS = DEFAULT_PKG + ".BaseDataBaseManagerImpl";

    private DataBaseM dataBaseM;

    public BaseDataBaseManagerImplJava(DataBaseM dataBaseM, String pkg) {
        this.dataBaseM = dataBaseM;
        this.pkg = pkg;
    }

    public String getDatabaseManagerClass() {
        return DATABASE_MANAGER_CLASS;
    }

    public String brewJava() {
        return "package " + DEFAULT_PKG + ";\n" +
                "\n" +
                "\n" +
                "import android.content.Context;\n" +
                "\n" +
                "import de.greenrobot.dao.AbstractDaoSession;\n" +
                "import " + pkg + ".DaoMaster;\n" +
                "import " + pkg + ".DaoSession;\n" +
                "\n" +
                "public class BaseDataBaseManagerImpl implements BaseDataBaseManager.SessionInter{\n" +
                "\n" +
                "    @Override\n" +
                "    public AbstractDaoSession getDaoSession(Context context) {\n" +
                "        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, \"" + dataBaseM.getName() + "\", null);\n" +
                "        DaoSession mDaoSession = new DaoMaster(helper.getWritableDatabase()).newSession();\n" +
                "        return mDaoSession;\n" +
                "    }\n" +
                "}";
    }
}
