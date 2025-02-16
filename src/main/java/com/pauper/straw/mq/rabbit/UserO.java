package com.pauper.straw.mq.rabbit;

import java.io.Serializable;

public class UserO implements Serializable {

    private String name;
    private int age;
    private AdderO adderO;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AdderO getAdderO() {
        return adderO;
    }

    public void setAdderO(AdderO adderO) {
        this.adderO = adderO;
    }
}
