package com.wyy.testactable.entity;

import com.wyy.actable.annotation.Column;
import com.wyy.actable.constants.MySqlDataType;

import java.util.Date;

/**
 * @Date: 20-1-26
 * @Author: wyy
 */

/**
 * @param <T> 主键类型
 */
public abstract class BaseEntity<T> {
    @Column(name = "id", type = MySqlDataType.BIGINT, length = 20, nullable = false)
    private T id;
    @Column(name = "user_id", type = MySqlDataType.BIGINT, length = 20, nullable = true)
    private long userId;
    @Column(name = "full_name", type = MySqlDataType.VARCHAR, length = 75, nullable = true)
    private String fullName;
    @Column(name = "create_date", type = MySqlDataType.DATETIME, nullable = true)
    private Date createDate;

    public BaseEntity() {
    }

    public BaseEntity(T id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    public T getId() {
        return id;
    }

    public BaseEntity<T> setId(T id) {
        this.id = id;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public BaseEntity setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public BaseEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public BaseEntity setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }
}
