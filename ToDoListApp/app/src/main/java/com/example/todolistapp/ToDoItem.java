package com.example.todolistapp;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.net.URI;
import java.util.GregorianCalendar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by JoshuaMabry on 10/23/16.
 */

public class ToDoItem implements Comparable<ToDoItem> {

    @SerializedName("taskName")
    private String taskName;
    @SerializedName("comment")
    private String comment;
    @SerializedName("category")
    private String category;
    @SerializedName("duDate")
    private Date dueDate;
    @SerializedName("modifiedDate")
    private Date modifiedDate;
    @SerializedName("checkBox")
    private Boolean checkBox;

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    @SerializedName("pathame")
    private String pathname;
    @SerializedName("catID")
    private String catID;


    public ToDoItem(Boolean checkBox, String taskName, String comment, String category, Date dueDate, Date modifiedDate, String pathname, String catID) {
        this.checkBox = checkBox;
        this.taskName = taskName;
        this.comment = comment;
        this.category = category;
        this.dueDate = dueDate;
        this.modifiedDate = modifiedDate;
        this.pathname = pathname;
        this.catID = catID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String title) {
        this.taskName = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCategory() {return category;}

    public void setCategory (String category) {this.category = category;}

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }


    public String getCatID() {
        return catID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    @Override
    public int compareTo(ToDoItem another) {
        return another.getDueDate().compareTo(getDueDate());
    }
}