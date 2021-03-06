package com.example.todolistapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by JoshuaMabry on 10/28/16.
 */

public class CategoryAdapter extends BaseAdapter {
    private ArrayList<Object> items;
    private LayoutInflater layoutInflater;
    private SimpleDateFormat formatter;


    // Define ints to determine the type of view we want to create
    private static final int TYPE_TODOITEM = 0;
    private static final int TYPE_CATEGORY = 1;

    // Construct our custom adapter
    public CategoryAdapter(Context context, ArrayList<Object> object) {
        this.items = object;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formatter = new SimpleDateFormat("MM/dd/yyyy");
    }

    // Get the number of views we will need to inflate. Should be the size of our items array
    @Override
    public int getCount() {
        return items.size();
    }

    // Get the position in our array we are at
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Get the position of our item in the array
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    // Determine the amount of separate views our adapter will need to handle
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // Determine the type of view we will need to use for the position in our item array
    @Override public int getItemViewType(int position) {
        if(getItem(position) instanceof ToDoItem) {
            return TYPE_TODOITEM;
        }

        return TYPE_CATEGORY;
    }

    // Enable or disabled the ability to interact with the view
    @Override public boolean isEnabled(int position) {
        return true;
    }

    // Determine the type of view we are creating, then insert the necessary info from our
    // items array
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if(convertView == null) {
            if(type == TYPE_TODOITEM) {
                convertView = layoutInflater.inflate(R.layout.todo_list_item_newlayout, parent, false);
            } else if (type == TYPE_CATEGORY) {
                convertView = layoutInflater.inflate(R.layout.todo_list_category, parent, false);

            }
        }

        if(type == TYPE_TODOITEM) {

            ToDoItem toDoItem = (ToDoItem) getItem(position);

            TextView title = (TextView) convertView.findViewById(R.id.note_title);
            TextView text = (TextView) convertView.findViewById(R.id.note_text);
            TextView date = (TextView) convertView.findViewById(R.id.note_date);

            title.setText(toDoItem.getTaskName());
            text.setText(toDoItem.getComment());
            date.setText(toDoItem.getDueDate().toString());

        } else if(type == TYPE_CATEGORY) {
            String categoryName = (String) getItem(position);
            TextView category = (TextView) convertView.findViewById(R.id.category);
            category.setText(categoryName);
        }
        return convertView;
    }

}
