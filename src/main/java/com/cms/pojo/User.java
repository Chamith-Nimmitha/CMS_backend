package com.cms.pojo;

public class User {

    private String id;
    private String name;
    private int age;

    public User(String id, String name, int age){
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }

    public int getAge(){
        return this.age;
    }



    public String toString(){
        return this.id + " " + this.name+ " " + this.age;
    }

}
