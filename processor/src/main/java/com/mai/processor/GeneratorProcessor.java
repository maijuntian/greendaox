package com.mai.processor;


import com.google.auto.service.AutoService;
import com.mai.annotate.Column;
import com.mai.annotate.DataBase;
import com.mai.annotate.Id;
import com.mai.annotate.ManyToMany;
import com.mai.annotate.ManyToOne;
import com.mai.annotate.OneToMany;
import com.mai.annotate.OneToOne;
import com.mai.annotate.Table;
import com.mai.annotate.Cascade;
import com.mai.bean.ColumnM;
import com.mai.bean.ManyToManyM;
import com.mai.bean.ToManyM;
import com.mai.bean.ToOneM;
import com.mai.java.BaseDataBaseManagerImplJava;
import com.mai.java.BeanJava;
import com.mai.java.DaoJava;
import com.mai.java.DaoMasterJava;
import com.mai.java.DaoSessionJava;
import com.mai.bean.DataBaseM;
import com.mai.bean.TableM;

import java.io.IOException;
import java.io.Writer;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;


@AutoService(Processor.class)
public class GeneratorProcessor extends AbstractProcessor {

    private final String DEFAULT_PKG = "com.mai.xgreendao";
    private final String DEFAULT_PKG_DAO = DEFAULT_PKG + ".dao";
    private final String DEFAULT_PKG_BEAN = DEFAULT_PKG + ".bean";


    private final boolean IS_DEBUG = false;

    static Map<String, String> types = new HashMap<>();
    static final String UNSUPPORT_TYPE = "Not support Byte[], please uses byte[]!";

    static {
        types.put("long", "java.lang.Long");
        types.put("int", "java.lang.Integer");
        types.put("boolean", "java.lang.Boolean");
        types.put("byte", "java.lang.Byte");
        types.put("short", "java.lang.Short");
        types.put("float", "java.lang.Float");
        types.put("double", "java.lang.Double");
        types.put("java.lang.Byte[]", UNSUPPORT_TYPE);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<String>();
        types.add(DataBase.class.getCanonicalName());
        types.add(Table.class.getCanonicalName());
        types.add(Column.class.getCanonicalName());
        types.add(Id.class.getCanonicalName());
        types.add(ManyToMany.class.getCanonicalName());
        types.add(ManyToOne.class.getCanonicalName());
        types.add(OneToMany.class.getCanonicalName());
        types.add(OneToOne.class.getCanonicalName());
        return types;
    }

    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
    }

    private void error(Element element, String message, Object... args) {
        if (args != null && args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
        throw new IllegalStateException(message);
    }

    private void error(Element element, Exception e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
        throw new IllegalStateException(e.getMessage());
    }

    private void error(String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message);
    }

    private void note(Element element, String message, Object... args) {
        if (IS_DEBUG) {
            if (args.length > 0) {
                message = String.format(message, args);
            }
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
        }
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private String getType(String type) throws IllegalStateException {
        String typeL = types.get(type);
        if (typeL == null)
            return type;
        if (typeL.equals(UNSUPPORT_TYPE))
            throw new IllegalStateException(typeL);
        return typeL;
    }

    private List<DataBaseM> parseDataBase(Set<? extends Element> dataBaseSet) {
        List<DataBaseM> dataBaseMs = new ArrayList<>();
        for (Element element : dataBaseSet) {
            DataBaseM dataBaseM = new DataBaseM();
            DataBase dataBase = element.getAnnotation(DataBase.class);
            dataBaseM.setName(dataBase.name());
            dataBaseM.setVersion(dataBase.version());
            dataBaseM.setKey(dataBase.key());
            note(element, dataBaseM.toString());
            dataBaseMs.add(dataBaseM);
        }
        return dataBaseMs;
    }

    private Map<String, TableM> parseTable(Set<? extends Element> tableSet) {
        Map<String, TableM> tables = new HashMap<>();
        for (Element element : tableSet) {
            TypeElement tel = (TypeElement) element;
            TableM tableM = new TableM();
            tableM.setElement(element);
            Table table = element.getAnnotation(Table.class);
            tableM.setType(tel.getQualifiedName().toString());
            tableM.setClazzName(tel.getSimpleName().toString());
            tableM.setName("".equals(table.name()) ? tableM.getClazzName().toLowerCase() : table.name());
            tableM.addCreateIndexs(table.createIndex());
            tableM.setMidTable(false);
            tableM.setDbKey(table.dbKey());
            tables.put(tableM.getType(), tableM);
            note(element, "表：" + tableM.toString());
        }
        return tables;
    }

    private void parseColoum(Set<? extends Element> coloumSet, Map<String, TableM> tables) {
        for (Element element : coloumSet) {
            try {
                // 获取该元素封装类型
                TypeElement enclosingElement = (TypeElement) element
                        .getEnclosingElement();
                // 拿到key
                String tableKey = enclosingElement.getQualifiedName().toString();
                TableM tableM = tables.get(tableKey);

                checkTable(element, tableM);

                VariableElement vel = (VariableElement) element;
                ColumnM columnM = new ColumnM();
                Column column = vel.getAnnotation(Column.class);
                columnM.setField(vel.getSimpleName().toString());
                columnM.setIsboolean(vel.getSimpleName().toString().equals("boolean"));
                columnM.setType(getType(vel.asType().toString()));
                columnM.setClazzName(columnM.getType().substring(columnM.getType().lastIndexOf(".") + 1));
                columnM.setName("".equals(column.name()) ? columnM.getField() : column.name());
                columnM.setNull(column.isNull());
                columnM.setPrimaryKey(false);
                columnM.setUnique(column.unique());
                tableM.addColoum(columnM);

                note(element, "列：" + columnM.toString());
            } catch (IllegalStateException e) {
                error(element, e.getMessage());
            }
        }
    }

    private void parseId(Set<? extends Element> idSet, Map<String, TableM> tables) {
        for (Element element : idSet) {
            try {
                // 获取该元素封装类型
                TypeElement enclosingElement = (TypeElement) element
                        .getEnclosingElement();
                // 拿到key
                String tableKey = enclosingElement.getQualifiedName().toString();
                TableM tableM = tables.get(tableKey);
                checkTable(element, tableM);

                VariableElement vel = (VariableElement) element;
                ColumnM columnM = new ColumnM();
                Id id = element.getAnnotation(Id.class);
                columnM.setField(vel.getSimpleName().toString());
                columnM.setType(getType(vel.asType().toString()));
                columnM.setClazzName(columnM.getType().substring(columnM.getType().lastIndexOf(".") + 1));
                columnM.setName("".equals(id.name()) ? columnM.getField() : id.name());
                columnM.setNull(false);
                columnM.setPrimaryKey(true);
                columnM.setAuto(id.autoIncrement());
                if (id.autoIncrement() && !(columnM.getClazzName().equals("Long") || columnM.getClazzName().equals("long"))) {
                    error(element, "AUTOINCREMENT is only available to primary key properties of type long/Long");
                }
                tableM.addColoum(columnM);
                tableM.setpK(columnM);

                note(element, "主键：" + columnM.toString());
            } catch (IllegalStateException e) {
                error(element, e.getMessage());
            }
        }
    }

    private void checkTableId(TableM tableM) {
        if (tableM.getpK() == null) {
            error(tableM.getElement(), tableM.getType() + " miss id Annotation");
        }
    }

    private void checkTable(Element element, TableM tableM) {
        if (tableM == null) {
            error(element, "miss table Annotation");
        }
    }

    private void parseOneToMany(Set<? extends Element> set, Map<String, TableM> tables) {
        for (Element element : set) {
            try {
                // 获取该元素封装类型
                TypeElement enclosingElement = (TypeElement) element
                        .getEnclosingElement();
                // 拿到key
                String tableKey = enclosingElement.getQualifiedName().toString();
                TableM tableM = tables.get(tableKey);
                checkTableId(tableM);

                VariableElement vel = (VariableElement) element;
                ToManyM toManyM = new ToManyM();
                OneToMany oneToMany = element.getAnnotation(OneToMany.class);
                TableM targetTable = tables.get(oneToMany.type());

                toManyM.setTargetTable(targetTable);
                toManyM.setTargetType(oneToMany.type());
                toManyM.setField(vel.getSimpleName().toString());
                toManyM.setIdProperty(tableM.getClazzName() + "Id");
                toManyM.setLazy(oneToMany.lazy());
                toManyM.setCascades(Arrays.asList(oneToMany.cascade()));
                tableM.addToMany(toManyM);
                note(element, "toManyM：" + toManyM.toString());
            } catch (IllegalStateException e) {
                error(element, e.getMessage());
            }
        }
    }

    private void parseManyToOne(Set<? extends Element> set, Map<String, TableM> tables) {
        for (Element element : set) {
            try {
                // 获取该元素封装类型
                TypeElement enclosingElement = (TypeElement) element
                        .getEnclosingElement();
                // 拿到key
                String tableKey = enclosingElement.getQualifiedName().toString();
                TableM tableM = tables.get(tableKey);
                checkTableId(tableM);

                VariableElement vel = (VariableElement) element;

                ManyToOne manyToOne = element.getAnnotation(ManyToOne.class);

                ToOneM toOneM = new ToOneM();
                toOneM.setTargetType(vel.asType().toString());
                TableM targetTableM = tables.get(toOneM.getTargetType());
                checkTableId(targetTableM);

                toOneM.setTargetTable(targetTableM);
                toOneM.setTargetIdType(targetTableM.getpK().getClazzName());
                toOneM.setField(vel.getSimpleName().toString());
                toOneM.setTargetIdUpperField(targetTableM.getpK().getUpperField());
                toOneM.setIdProperty(targetTableM.getClazzName() + "Id");
                toOneM.setLazy(manyToOne.lazy());
                toOneM.setCascades(Arrays.asList(manyToOne.cascade()));
                tableM.addToOne(toOneM);

                note(element, "toOneM：" + toOneM.toString());
            } catch (IllegalStateException e) {
                error(element, e.getMessage());
            }
        }
    }

    /**
     * 一对一需要 创建索引
     */
    private void parseOneToOne(Set<? extends Element> set, Map<String, TableM> tables) {
        for (Element element : set) {
            try {
                // 获取该元素封装类型
                TypeElement enclosingElement = (TypeElement) element
                        .getEnclosingElement();
                // 拿到key
                String tableKey = enclosingElement.getQualifiedName().toString();
                TableM tableM = tables.get(tableKey);
                checkTableId(tableM);

                VariableElement vel = (VariableElement) element;
                ToOneM oneToOneM = new ToOneM();
                oneToOneM.setTargetType(vel.asType().toString());
                TableM targetTableM = tables.get(oneToOneM.getTargetType());
                checkTableId(targetTableM);


                OneToOne oneToOne = element.getAnnotation(OneToOne.class);
                oneToOneM.setTargetTable(targetTableM);
                oneToOneM.setTargetIdType(targetTableM.getpK().getClazzName());
                oneToOneM.setField(vel.getSimpleName().toString());
                oneToOneM.setTargetIdUpperField(targetTableM.getpK().getUpperField());
                oneToOneM.setIdProperty(targetTableM.getClazzName() + "Id");
                oneToOneM.setLazy(oneToOne.lazy());
                oneToOneM.setCascades(Arrays.asList(oneToOne.cascade()));

                tableM.addToOne(oneToOneM);

                /**
                 * 创建索引
                 */

                tableM.addOneToOneIndex(targetTableM);

                note(element, "oneToOneM：" + oneToOneM.toString());
            } catch (IllegalStateException e) {
                error(element, e.getMessage());
            }
        }
    }

    /**
     * 多对多，需要新建中间表
     */
    private void parseManyToMany(Set<? extends Element> set, Map<String, TableM> tables) {
        for (Element element : set) {
            try {
                // 获取该元素封装类型
                TypeElement enclosingElement = (TypeElement) element
                        .getEnclosingElement();
                // 拿到key
                String tableKey = enclosingElement.getQualifiedName().toString();
                TableM tableM = tables.get(tableKey);
                checkTableId(tableM);

                ManyToMany manyToMany = element.getAnnotation(ManyToMany.class);
                TableM targetTableM = tables.get(manyToMany.type());
                checkTableId(targetTableM);

                VariableElement vel = (VariableElement) element;
                ManyToManyM manyToManyM = new ManyToManyM();
                manyToManyM.setTargetTable(targetTableM);
                manyToManyM.setField(vel.getSimpleName().toString());
                manyToManyM.setInverse(manyToMany.inverse());
                manyToManyM.setLazy(manyToMany.lazy());
                manyToManyM.setCascades(Arrays.asList(manyToMany.cascade()));

                //新建中间表
                manyToManyM.setMidTable(newMidTable(tableM, targetTableM, tables));
                tableM.addManyToMany(manyToManyM);

                note(element, "ManytoManyM：" + manyToManyM.toString());
            } catch (IllegalStateException e) {
                error(element, e.getMessage());
            }
        }
    }

    /**
     * 新建中间表
     *
     * @param tableM
     * @param targetTableM
     * @param tables
     * @return
     */
    private TableM newMidTable(TableM tableM, TableM targetTableM, Map<String, TableM> tables) {
        String tableType1 = DEFAULT_PKG_BEAN + "." + tableM.getClazzName() + "_" + targetTableM.getClazzName() + "_";
        String tableType2 = DEFAULT_PKG_BEAN + "." + targetTableM.getClazzName() + "_" + tableM.getClazzName() + "_";

        if (tables.containsKey(tableType1)) {
            return tables.get(tableType1);
        }
        if (tables.containsKey(tableType2)) {
            return tables.get(tableType2);
        }

        TableM midTable = new TableM();
        midTable.setDbKey(tableM.getDbKey());
        midTable.setType(tableType1);
        midTable.setClazzName(tableM.getClazzName() + "_" + targetTableM.getClazzName() + "_");
        midTable.setName(midTable.getClazzName().toLowerCase());

        ColumnM id = new ColumnM();
        id.setType(Long.class.toString());
        id.setClazzName(Long.class.getSimpleName());
        id.setName("id");
        id.setField("id");
        id.setAuto(true);
        id.setPrimaryKey(true);
        id.setIndex(0);
        midTable.setpK(id);
        midTable.addColoum(id);

        ColumnM tableId = new ColumnM();
        tableId.setType(tableM.getpK().getType());
        tableId.setClazzName(tableM.getpK().getClazzName());
        tableId.setName(tableM.getClazzName().toLowerCase() + "Id");
        tableId.setField(tableM.getClazzName().toLowerCase() + "Id");
        tableId.setIndex(1);
        tableId.setNull(false);
        midTable.addColoum(tableId);


        ColumnM targetTableId = new ColumnM();
        targetTableId.setType(targetTableM.getpK().getType());
        targetTableId.setClazzName(targetTableM.getpK().getClazzName());
        targetTableId.setName(targetTableM.getClazzName().toLowerCase() + "Id");
        targetTableId.setField(targetTableM.getClazzName().toLowerCase() + "Id");
        targetTableId.setIndex(1);
        targetTableId.setNull(false);
        midTable.addColoum(targetTableId);

        midTable.setMidTable(true);
        note(tableM.getElement(), "midTableM:" + midTable.toString());
        tables.put(tableType1, midTable);
        return midTable;
    }

    private void writeFile(String fileName, Element element, String content) {
        try {
            JavaFileObject jfo = filer.createSourceFile(fileName, element);
            Writer writer = jfo.openWriter();
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            error(element, "Unable to write view binder for type %s: %s", element,
                    e.getMessage());
        }
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> dataBaseSet = roundEnv.getElementsAnnotatedWith(DataBase.class);
        Set<? extends Element> tableSet = roundEnv.getElementsAnnotatedWith(Table.class);
        Set<? extends Element> idSet = roundEnv.getElementsAnnotatedWith(Id.class);
        Set<? extends Element> coloumSet = roundEnv.getElementsAnnotatedWith(Column.class);
        Set<? extends Element> manyToManySet = roundEnv.getElementsAnnotatedWith(ManyToMany.class);
        Set<? extends Element> manyToOneSet = roundEnv.getElementsAnnotatedWith(ManyToOne.class);
        Set<? extends Element> OneToManySet = roundEnv.getElementsAnnotatedWith(OneToMany.class);
        Set<? extends Element> OneToOneSet = roundEnv.getElementsAnnotatedWith(OneToOne.class);

        List<DataBaseM> dataBases = parseDataBase(dataBaseSet);
        Map<String, TableM> tables = parseTable(tableSet);
        if (dataBases.size() == 0 && tableSet.size() == 0) {
            return false;
        }
        if (dataBases.size() == 0) {
            error("Miss DataBase Annotation");
        }
        if (tables.size() == 0) {
            error("Miss Table Annotation");
        }
        parseId(idSet, tables);
        parseColoum(coloumSet, tables);
        parseManyToOne(manyToOneSet, tables);
        parseOneToMany(OneToManySet, tables);
        parseOneToOne(OneToOneSet, tables);

        parseManyToMany(manyToManySet, tables);


        List<TableM> tableMListTemp = new ArrayList<>(tables.values());

        for (TableM tbM : tableMListTemp) {
            for (DataBaseM dbM : dataBases) {
                if (dbM.getKey() == tbM.getDbKey()) {
                    dbM.addTable(tbM);
                }
            }
        }

        for (DataBaseM dbM : dataBases) {
            List<TableM> tableMList = dbM.getTableMs();
            /**************java文件输出*******************/
            writeFile(DEFAULT_PKG_DAO + ".DaoSession" + dbM.getKey(), tableMList.get(0).getElement(), new DaoSessionJava(DEFAULT_PKG_DAO, tableMList).brewJave());

            for (TableM tableM : tableMList) {
                DaoJava daoJava = new DaoJava(DEFAULT_PKG_DAO, tableM);
                writeFile(DEFAULT_PKG_DAO + "." + daoJava.getDaoName(), tableM.getElement(), daoJava.brewJava());
                if (tableM.isMidTable()) { //中间表需要创建bean
                    BeanJava beanJava = new BeanJava(DEFAULT_PKG_BEAN, tableM);
                    writeFile(DEFAULT_PKG_BEAN + "." + tableM.getClazzName(), tableM.getElement(), beanJava.brewJava());
                }
            }

            writeFile(DEFAULT_PKG_DAO + ".DaoMaster" + dbM.getKey(), tableMList.get(0).getElement(), new DaoMasterJava(tableMList, DEFAULT_PKG_DAO, dbM).brewJava());

            BaseDataBaseManagerImplJava dataBaseManagerImplJava = new BaseDataBaseManagerImplJava(dbM, DEFAULT_PKG_DAO);
            writeFile(dataBaseManagerImplJava.getDatabaseManagerClass(), tableMList.get(0).getElement(), dataBaseManagerImplJava.brewJava());
            /**************java文件输出*******************/
        }

        return true;
    }

}