package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.OneToMany;
import com.mai.annotate.Table;
import com.mai.greendaox.XDBManager;

import java.util.List;

/**
 * Created by mai on 16/8/19.
 */
@Table
public class Bag {

    @Id(autoIncrement = true)
    private Long id;

    @Column
    private String name;

    @OneToMany(type = "com.mai.greendaox.bean.Book", lazy = false/**取消懒加载，当你查询Bag数据的时候，book的数据也会同时查询*/)
    private List<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        if (books == null)
            books = XDBManager.getMany(this, Book.class, id);
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Bag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}
