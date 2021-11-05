package com.example.parentsupportapp.model;

import java.util.ArrayList;
import java.util.List;

public class Family {
    private List<Child> children;
    private static Family instance;

    public static Family getInstance() {
        if (instance == null) {
            instance = new Family();
        }
        return instance;
    }

    private Family(){
        this.children = new ArrayList<>();
    }

    public List<Child> getChildren() {
        return children;
    }

    public List<String> getChildrenInString() {
        List<String> ls = new ArrayList<>();
        for (int i = 0; i < this.children.size(); i++) {
            ls.add(children.get(i).getFirstName());
        }
        return ls;
    }

    public void addChild(String fName) {
        this.children.add(new Child(fName));
    }

    public void removeChild(int pos) {
        this.children.remove(pos);
    }
}
