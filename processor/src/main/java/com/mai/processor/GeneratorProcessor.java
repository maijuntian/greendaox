package com.mai.processor;


import com.google.auto.service.AutoService;
import com.mai.annotate.Column;
import com.mai.annotate.DataBase;
import com.mai.annotate.Id;
import com.mai.annotate.Table;
import com.mai.bean.ColumnM;
import com.mai.java.BaseDataBaseManagerImplJava;
import com.mai.java.DaoJava;
import com.mai.java.DaoMasterJava;
import com.mai.java.DaoSessionJava;
import com.mai.bean.DataBaseM;
import com.mai.bean.TableM;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
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

    private final String DEFAULT_PKG = "com.mai.xgreendao.dao";

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<String>();
        types.add(DataBase.class.getCanonicalName());
        types.add(Table.class.getCanonicalName());
        types.add(Column.class.getCanonicalName());
        types.add(Id.class.getCanonicalName());
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
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private void error(Element element, Exception e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage(), element);
    }


    private void note(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
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

    private DataBaseM parseDataBase(Set<? extends Element> dataBaseSet) {
        DataBaseM dataBaseM = new DataBaseM();
        for (Element element : dataBaseSet) {
            DataBase dataBase = element.getAnnotation(DataBase.class);
            dataBaseM.setName(dataBase.name());
            dataBaseM.setVersion(dataBase.version());
            note(element, dataBaseM.toString());
        }
        return dataBaseM;
    }

    private Map<String, TableM> parseTable(Set<? extends Element> tableSet) {
        Map<String, TableM> tables = new HashMap<>();
        for (Element element : tableSet) {
            TypeElement tel = (TypeElement) element;
            TableM tableM = new TableM();
            tableM.setElement(element);
            Table table = element.getAnnotation(Table.class);
            tableM.setName(table.name());
            tableM.setType(tel.getQualifiedName().toString());
            tableM.setClazzName(tel.getSimpleName().toString());
            tableM.setName("".equals(table.name()) ? tableM.getClazzName().toLowerCase() : table.name());
            tableM.setCreateIndex(table.createIndex());
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

                VariableElement vel = (VariableElement) element;
                ColumnM columnM = new ColumnM();
                Column column = vel.getAnnotation(Column.class);
                columnM.setField(vel.getSimpleName().toString());
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
                if(id.autoIncrement() && !(columnM.getClazzName().equals("Long") || columnM.getClazzName().equals("long"))){
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

        DataBaseM dataBase = parseDataBase(dataBaseSet);
        Map<String, TableM> tables = parseTable(tableSet);
        if (tables.size() == 0) {
            return false;
        }
        List<TableM> tableMList = new ArrayList<>(tables.values());
        if (dataBase == null) {
            error(tables.get(0).getElement(), "Miss DataBase Annotation");
        }
        parseId(idSet, tables);
        parseColoum(coloumSet, tables);
        writeFile(DEFAULT_PKG + ".DaoSession", tableMList.get(0).getElement(), new DaoSessionJava(DEFAULT_PKG, tableMList).brewJave());

        for (TableM tableM : tableMList) {
            DaoJava daoJava = new DaoJava(DEFAULT_PKG, tableM);
            writeFile(DEFAULT_PKG + "." + daoJava.getDaoName(), tableM.getElement(), daoJava.brewJava());
        }

        writeFile(DEFAULT_PKG + ".DaoMaster", tableMList.get(0).getElement(), new DaoMasterJava(tableMList, DEFAULT_PKG, dataBase).brewJava());

        BaseDataBaseManagerImplJava dataBaseManagerImplJava = new BaseDataBaseManagerImplJava(dataBase, DEFAULT_PKG);
        writeFile(dataBaseManagerImplJava.getDatabaseManagerClass(), tableMList.get(0).getElement(), dataBaseManagerImplJava.brewJava());
        return false;
    }

}