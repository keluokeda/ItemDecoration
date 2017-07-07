package com.ke.itemdecoration;


public class NameBean {
    private String name;

    public NameBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return String.valueOf(name.charAt(0));
    }
}
