package com.infideap.firebaseexample.entity;

/**
 * Created by Shiburagi on 17/09/2017.
 */

public class Person {
    public int noOfCars;
    public double salary;
    public String name;
    public String key;

    public Person(){

    }
    public Person(String name, double salary, int noOfCars) {
        this.name = name;
        this.salary = salary;
        this.noOfCars = noOfCars;
    }
}
