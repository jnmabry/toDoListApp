package com.example.todolistapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by JoshuaMabry on 10/23/16.
 */

public class ToDoItemArrayAdapter extends BaseAdapter {

    private int resource;
    public List<ToDoItem> parkingList;
    public ArrayList<ToDoItem> toDoItems;
    private LayoutInflater inflater;
    private SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public Context context;


    public class ViewHolder {
        TextView taskName, dueDate,lastModified, category;
    }


    @Override public boolean isEnabled(int position) {
        return true;
    }



    public ToDoItemArrayAdapter(Context context, int resource, List<ToDoItem> apps){

        this.parkingList = apps;
        this.resource = resource;
//        this.toDoItems = objects;
        toDoItems = new ArrayList<ToDoItem>();
        toDoItems.addAll(parkingList);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        View toDoItemsRow = convertView;
        ViewHolder viewHolder;

        if (toDoItemsRow == null) {

            toDoItemsRow = inflater.inflate(resource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.taskName = (TextView) toDoItemsRow.findViewById(R.id.taskName);
            viewHolder.dueDate = (TextView) toDoItemsRow.findViewById(R.id.dueDate);
            viewHolder.lastModified = (TextView) toDoItemsRow.findViewById(R.id.lastModified);
            viewHolder.category = (TextView) toDoItemsRow.findViewById(R.id.category);
            toDoItemsRow.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.taskName.setText(parkingList.get(position).getTaskName());
        viewHolder.dueDate.setText(formatter.format(parkingList.get(position).getDueDate()));
        viewHolder.lastModified.setText(formatter.format(parkingList.get(position).getModifiedDate()));
        viewHolder.category.setText(parkingList.get(position).getCategory());



        return toDoItemsRow;

    }






    //Where we filter our results from search

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        parkingList.clear();

        if (charText.length() == 0) {
            parkingList.addAll(toDoItems);

        } else {
            for (ToDoItem toDoItem : toDoItems) {
                if (charText.length() != 0 && toDoItem.getTaskName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    parkingList.add(toDoItem);
                }
//
//                else if (charText.length() != 0 && toDoItem.getCategory().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    parkingList.add(toDoItem);
//                }
            }
        }
        notifyDataSetChanged();
    }



    public void updateAdapter(ArrayList<ToDoItem> toDoItems){
        this.toDoItems = toDoItems;
        super.notifyDataSetChanged();
    }

}
