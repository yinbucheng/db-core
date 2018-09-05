package cn.intellif.db.core.business.entity.entity;

import cn.intellif.db.core.base.annotation.*;

import java.io.Serializable;

@Table(tableName = "t_test",schemaName = "test2")
public class User implements Serializable {
    @Id(strategy = Strategy.AUTO_INCREMENT)
    private Long id;
    @Column(columnName = "name")
    private String name;
    @Terminal
    private Integer age;
    @Column(columnName = "tel_phone")
    private String telPhone;

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
