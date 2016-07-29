package com.mai.greendaox;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mai.annotate.DataBase;
import com.mai.greendaox.bean.Car;
import com.mai.greendaox.bean.Card;
import com.mai.greendaox.bean.Children;
import com.mai.greendaox.bean.Parent;
import com.mai.xgreendao.bean.Children_Parent_;
import com.mai.xgreendao.dao.CarDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.AbstractDao;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
//                testOneToMany();
//                testCapacity();
//                testManyToMany();
//                testCascadeDelete();
//                testCascadeInsert();
//                testCascadeDeleteOne();
                testCascadeUpdate();
                return null;
            }
        }.execute();

    }

    private void testCapacity() {
        DataBaseManager.getParentDao().deleteAll();
        List<Parent> parentList = new ArrayList<Parent>();
        for (int i = 0; i < 200000; i++) {
            Parent parent = new Parent();
            parent.setName("parent" + i);
            parent.setSex(1);
            parent.setAddress("test address");
            parentList.add(parent);
        }

        long time = System.currentTimeMillis();
        DataBaseManager.getParentDao().insertInTx(parentList);
        long time2 = System.currentTimeMillis();
        MLog.log("插入花费时间：" + (time2 - time));
        List<Parent> parents = DataBaseManager.getParentDao().loadAll();
        long time3 = System.currentTimeMillis();
        MLog.log("查询 " + parents.size() + "花费时间：" + (time3 - time2));
    }

    private void testOneToMany() {

        AbstractDao<Children, Long> childrenDao = DataBaseManager.getChildrenDao();
        AbstractDao<Car, Long> carDao = DataBaseManager.getCarDao();

        /*********插入****************/
        List<Car> cars = new ArrayList<>();
        List<Children> sons = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            Children son = new Children();
            son.setName("儿子" + i);
            son.setSex(1);
            Car car = new Car();
            car.setName("大卡车" + i);
            car.setChildren(son);
            Car bus = new Car();
            bus.setName("大巴士" + i);
            bus.setChildren(son);

            sons.add(son);
            cars.add(car);
            cars.add(bus);
        }

        long lastTime = System.currentTimeMillis();
        childrenDao.insertInTx(sons);
        carDao.insertOrReplaceInTx(cars);
        MLog.log("插入耗时：" + (System.currentTimeMillis() - lastTime));
        lastTime = System.currentTimeMillis();

        /*********查询****************/
        DataBaseManager.clear(); //清除缓存
        List<Children> childrens = childrenDao.loadAll();
        List<Car> updateCars = new ArrayList<>();
        for (Children children : childrens) {
            List<Car> cars2 = children.getCars();
            MLog.log(cars2.toString());
            updateCars.addAll(cars2);
        }


        /***********修改*************/
        for (Car car : updateCars) {
            car.setName("车子");
        }
        carDao.insertOrReplaceInTx(updateCars);
//        carDao.updateInTx(updateCars);

        MLog.log("查询耗时：" + (System.currentTimeMillis() - lastTime));

        List<Car> afterUpdateCars = carDao.loadAll();
        for (Car chi : afterUpdateCars) {
            MLog.log(chi.toString() + "   " + chi.getChildren().toString());
        }

        /**********删除*************/
        carDao.deleteInTx(afterUpdateCars);
        childrenDao.deleteInTx(childrens);
    }

    private void testOneToOne() {

        AbstractDao<Children, Long> childrenDao = DataBaseManager.getChildrenDao();
        AbstractDao<Card, String> cardDao = DataBaseManager.getCardDao();
        List<Children> childrens = new ArrayList<>();
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Children children = new Children();
            children.setName("mai" + i);
            children.setSex(1);

            Card card = new Card();
            card.setDate(new Date());
            card.setId("AA20160714198273" + i);

            children.setCard(card);
            card.setChildren(children);

            childrens.add(children);
            cards.add(card);
        }

        childrenDao.insertInTx(childrens);
        cardDao.insertInTx(cards);

        DataBaseManager.clear(); //清除缓存


        List<Children> childs = childrenDao.loadAll();
        for (Children children : childs) {
            MLog.log(children.toString());
            MLog.log(children.getCard().toString());
        }
    }

    private void testManyToMany() {


        AbstractDao<Children, Long> childrenDao = DataBaseManager.getChildrenDao();
        AbstractDao<Parent, Long> parentDao = DataBaseManager.getParentDao();
        childrenDao.deleteAll();
        parentDao.deleteAll();

        /**
         * 插入
         */
        Parent father = new Parent();
        father.setName("father");
        father.setSex(1);
        father.setAddress("广州市天河区体育西路");

        Parent mother = new Parent();
        mother.setName("mother");
        mother.setSex(0);
        mother.setAddress("广州市天河区体育西路");

        List<Parent> parents = new ArrayList<>();
        parents.add(father);
        parents.add(mother);

        List<Children> childrens = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Children children = new Children();
            children.setName("child" + i);
            children.setSex(1);
            children.setParents(parents);
            childrens.add(children);
        }

        father.setChildrens(childrens);
        mother.setChildrens(childrens);

        parentDao.insertInTx(parents);
        childrenDao.insertInTx(childrens);

        DataBaseManager.clear();

        List<Parent> pars = parentDao.loadAll();
        MLog.log(pars.toString());
       /* for (Parent parent : pars){
            MLog.log(parent.getChildrens().toString());
        }*/


        /**
         * 更新
         */
        /*List<Children> updateChilds = childrenDao.queryRaw("where id in (?, ?)", new String[]{"19", "20"});
        List<Parent> updateParents = parentDao.queryRaw("where id in (?, ?)", new String[]{"3", "4"});
        MLog.log(updateChilds.toString());
        MLog.log(updateParents.toString());
        for (Children c : updateChilds){
            c.setParents(updateParents);
        }
        childrenDao.updateInTx(updateChilds);*/


        /**
         * 删除
         */
//        childrenDao.deleteByKeyInTx(1L, 2L);
//        parentDao.deleteByKeyInTx(7L, 8L);
//        childrenDao.deleteAll();


        List<Children_Parent_> children_parent_s = DataBaseManager.getDao(Children_Parent_.class).loadAll();

        for (Children_Parent_ children_parent_ : children_parent_s) {
            MLog.log(children_parent_.getId() + "  " + children_parent_.getChildrenId() + "  " + children_parent_.getParentId());
        }
    }

    public void testCascadeDelete(){
        AbstractDao<Children, Long> childrenDao = DataBaseManager.getChildrenDao();
        childrenDao.deleteAll();
    }

    public void testCascadeDeleteOne(){
        AbstractDao<Children, Long> childrenDao = DataBaseManager.getChildrenDao();
        childrenDao.deleteByKey(51L);

        DataBaseManager.clear();
        MLog.log(childrenDao.loadAll().toString());
    }

    public void testCascadeUpdate(){
        AbstractDao<Children, Long> childrenDao = DataBaseManager.getChildrenDao();

        Children children = childrenDao.load(52L);
        children.setName("wo bei gai ming le");
        for (Car car : children.getCars()){
            car.setName("wo ye bei gai le");
        }
        childrenDao.update(children);
        DataBaseManager.clear();
        MLog.log(childrenDao.loadAll().toString());
    }



    public void testCascadeInsert() {

        /**
         * testinsert
         */
        AbstractDao<Children, Long> childrenDao = DataBaseManager.getChildrenDao();
        AbstractDao<Card, String> cardDao = DataBaseManager.getCardDao();

        List<Children> childrens = new ArrayList<>();
        List<Parent> parents = new ArrayList<>();
        Parent father = new Parent();
        father.setName("father");
        father.setSex(1);
        father.setAddress("广州市天河区体育西路");

        Parent mother = new Parent();
        mother.setName("mother");
        mother.setSex(0);
        mother.setAddress("广州市天河区体育西路");

        parents.add(father);
        parents.add(mother);
        for (int i = 0; i < 10; i++) {
            Children children = new Children();
            children.setName("mai" + i);
            children.setSex(1);

            Card card = new Card();
            card.setDate(new Date());
            card.setId("AA20160714198273" + i);
            card.setChildren(children);

            List<Car> cars = new ArrayList<>();
            Car car1 = new Car();
            car1.setName("car1");
            car1.setChildren(children);

            Car car2 = new Car();
            car2.setName("car2");
            car2.setChildren(children);

            cars.add(car1);
            cars.add(car2);


            children.setParents(parents);
            children.setCard(card);
            children.setCars(cars);
            childrens.add(children);
        }

        father.setChildrens(childrens);
        mother.setChildrens(childrens);

        childrenDao.insertOrReplaceInTx(childrens);

        DataBaseManager.clear(); //清除缓存


        List<Children> childs = childrenDao.loadAll();
        MLog.log(childs.toString());

        List<Card> cards = cardDao.loadAll();
        MLog.log(cards.toString());
    }
}
