package com.example.todolistapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import java.util.ArrayList;

/**
 * Created by JoshuaMabry on 10/27/16.
 */

public class Category {
    @SerializedName("name")
    private String name;

    @SerializedName("notes")
    public ArrayList<ToDoItem> notes;

    public Category(String name, ArrayList<ToDoItem> notes) {
        this.name = name;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

