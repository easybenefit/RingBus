package com.entity;

/**
 * Created by handgunbreak@gmail.com on 16/7/16.
 * Mail: handgunbreak@gmail.com
 * Copyright: handgunbreak@gmail.com(2016)
 * Description:
 */
public class DoubleEvent {


    public int age = 10;
    public String sex = "男";

    public DoubleEvent(int age, String sex) {

        this.age = age;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "DoubleEvent{" +
                "age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }
}
