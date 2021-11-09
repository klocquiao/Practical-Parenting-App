package com.example.parentsupportapp.model;

import android.content.Context;
import android.util.Log;

import com.example.parentsupportapp.ChildConfigActivity;
import com.example.parentsupportapp.HistoryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Family {
    private List<Child> children;
    private static Family instance;
    private static Context context;

    public static Family getInstance(Context context) {
        if (instance == null) {
            instance = new Family(context);
        }
        return instance;
    }

    private Family(Context context){
        this.context = context;
        String jsonFamily = ChildConfigActivity.getFamily(context);

        if (jsonFamily == ChildConfigActivity.EMPTY_PREF) {
            children = new ArrayList<>();
        }
        else {
            children = deserializeFamily(jsonFamily);
        }
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
        ChildConfigActivity.saveChildConfigPrefs(context, this);
    }

    public void removeChild(int pos) {
        this.children.remove(pos);
        ChildConfigActivity.saveChildConfigPrefs(context, this);
    }

    public boolean isNoChildren() {
        return children.isEmpty();
    }

    private List<Child> deserializeFamily(String jsonFamily) {
        Type type = new TypeToken<List<Child>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonFamily, type);
    }
}
