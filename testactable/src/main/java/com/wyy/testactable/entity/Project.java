package com.wyy.testactable.entity;

import com.wyy.actable.annotation.Column;
import com.wyy.actable.annotation.Index;
import com.wyy.actable.annotation.Table;
import com.wyy.actable.annotation.Unique;
import com.wyy.actable.constants.MySqlDataType;

/**
 * @Date: 20-2-6
 * @Author: wyy
 */
@Table(name = "project")
public class Project extends BaseEntity<Long> {

    private static final long serialVersionUID = 5199200306752426433L;

    @Index
    @Column(name = "name", type = MySqlDataType.VARCHAR, length = 666, defaultValue = "项目1", comment = "项目名12")
    private String name;

    @Index(name = "Idx_desc_comm")
    @Column(name = "description", type = MySqlDataType.VARCHAR, length = 200)
    private String description;

    @Index(name = "Idx_desc_comm")
    @Column(name = "common", type = MySqlDataType.BIT, length = 1)
    private boolean common;

    @Unique
    @Column(name = "price", type = MySqlDataType.DOUBLE, length = 10, decimal = 3)
    private double price;

    @Unique(name = "uni_ab")
    @Column(name = "namea", type = MySqlDataType.VARCHAR, length = 111)
    private String namea;

    @Unique(name = "uni_ab")
    @Column(name = "nameb", type = MySqlDataType.VARCHAR, length = 111)
    private String nameb;

//    @Column(name = "namec", type = MySqlDataType.VARCHAR, length = 111)
//    private String namec;
//
//
//    public String getNamec() {
//        return namec;
//    }
//
//    public Project setNamec(String namec) {
//        this.namec = namec;
//        return this;
//    }

    public String getNamea() {
        return namea;
    }

    public Project setNamea(String namea) {
        this.namea = namea;
        return this;
    }

    public String getNameb() {
        return nameb;
    }

    public Project setNameb(String nameb) {
        this.nameb = nameb;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public Project setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    public boolean isCommon() {
        return common;
    }

    public Project setCommon(boolean common) {
        this.common = common;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public Project setPrice(double price) {
        this.price = price;
        return this;
    }
}
