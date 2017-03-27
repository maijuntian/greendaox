package com.mai.greendaox.bean;

import com.mai.annotate.Column;
import com.mai.annotate.Id;
import com.mai.annotate.Table;

/**
 * Created by mai on 17/2/15.
 */
@Table(dbKey = 1)
public class Mai {
    @Id
    Long id;

    @Column
    String name;

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
}
