package com.mai.greendaox;

import android.content.Context;

import com.mai.annotate.DataBase;
import com.mai.annotate.Table;
import com.mai.greendaox.bean.AllType;
import com.mai.greendaox.bean.Bag;
import com.mai.greendaox.bean.Book;
import com.mai.greendaox.bean.Boss;
import com.mai.greendaox.bean.Card;
import com.mai.greendaox.bean.ClassRoom;
import com.mai.greendaox.bean.Employees;
import com.mai.greendaox.bean.Order;
import com.mai.greendaox.bean.Product;
import com.mai.greendaox.bean.School;
import com.mai.greendaox.bean.Student;
import com.mai.xgreendao.base.DBManager;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by mai on 16/6/30.
 */
@DataBase(name = "greendaotest", version = 10)
public class XDBManager extends DBManager {


    public static AbstractDao<Card, String> getCardDao() {
        return getDao(Card.class);
    }

    public static AbstractDao<AllType, Long> getAllTypeDao() {
        return getDao(AllType.class);
    }

    public static AbstractDao<Student, String> getStudentDao() {
        return getDao(Student.class);
    }

    public static AbstractDao<Boss, Long> getBossDao() {
        return getDao(Boss.class);
    }

    public static AbstractDao<Employees, String> getEmployeesDao() {
        return getDao(Employees.class);
    }

    public static AbstractDao<Product, Long> getProductDao() {
        return getDao(Product.class);
    }

    public static AbstractDao<Order, Long> getOrderDao() {
        return getDao(Order.class);
    }

    public static AbstractDao<School, Long> getSchoolDao() {
        return getDao(School.class);
    }

    public static AbstractDao<ClassRoom, Long> getClassRoomDao() {
        return getDao(ClassRoom.class);
    }
    public static AbstractDao<Book, Long> getBookDao() {
        return getDao(Book.class);
    }
    public static AbstractDao<Bag, Long> getBagDao() {
        return getDao(Bag.class);
    }
}
