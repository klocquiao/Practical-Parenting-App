package com.example.parentsupportapp.model;

import java.util.ArrayList;
import java.util.List;

public class Family {
    private List<Child> children;

    //singleton support
    private static Family instance;

    private Family(){
        //this.lastName = null;
        this.children = new ArrayList<>();
    }

    /*
    private Family(String lName, ArrayList<Child> children) {
        //this.lastName = lName;
        this.children = children;
    }

     */

    public static Family getInstance() {
        if (instance == null) {
            instance = new Family();
        }
        return instance;
    }

    // getters and setters

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

    // Adding children

    /*
    public int addChild(String fName, String lName) {
        if (this.lastName == null || this.lastName == "") {
            this.lastName = lName;
        }
        else if (lName != this.lastName) {
            return -1;
        }
        this.children.add(new Child(fName, lName));
        return 0;

        //addChildrenToFile(children);
    }

     */

    public void addChild(Child c) {
        this.children.add(c);
        //addChildrenToFile(children);
    }

    public void addChild(String fName) {
        this.children.add(new Child(fName));
        //addChildrenToFile(children);
    }

    // Removing children

    public void removeChild(String fName) {
        int index = -1;
        for (int i = 0; i < this.children.size(); i++) {
            if (this.children.get(i).getFirstName()==fName) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            this.children.remove(index);
            //addChildrenToFile(children);
        }
    }

    public void removeChild(Child c) {
        this.children.remove(c);
        //addChildrenToFile(children);
    }

    /*
    public void addChildrenToFile(List<Child> ls) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("list of names"));
            //
            for (int i = 0; i < this.children.size(); i++) {
                bw.write(ls.get(i).getFirstName() + " " + ls.get(i).getLastName() + "\n");
            }
            bw.close();
        } catch (Exception ex) {
            return;
        }
    }

     */
}
