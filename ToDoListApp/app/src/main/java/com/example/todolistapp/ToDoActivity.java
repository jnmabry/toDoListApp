package com.example.todolistapp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ToDoActivity extends AppCompatActivity {


    Gson gson = new Gson();
    private ListView toDoList;
    private ToDoItemArrayAdapter toDoItemArrayAdapter;
    public ArrayList<ToDoItem> toDoArray;
    public List<ToDoItem> parkingList;
    private SharedPreferences toDoPrefs;
    private String filename = "MainWriteFile";
    List<ToDoItem> noteLists = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private ArrayList<Object> allItems = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private int catNumber;
    int oldNotePosition;
    public SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar setup
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toDoPrefs = getPreferences(Context.MODE_PRIVATE);
        gson = new Gson();
        setupToDo();

        //Sort the Array List by Date
        Collections.sort(toDoArray);

        //Ties variable to the view
        // If not showing on screen change this
        toDoList = (ListView) findViewById(R.id.listView);
        updateAllItems();

        // New Category Adapter
        categoryAdapter = new CategoryAdapter(this, allItems);
        toDoList.setAdapter(categoryAdapter);


        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                allItems.get(position);
                oldNotePosition = position;


                ToDoItem toDoItem = (ToDoItem)allItems.get(position);
                Intent intent = new Intent(ToDoActivity.this, ToDoDetailActivity.class);


                intent.putExtra("Title", toDoItem.getTaskName());
                intent.putExtra("Date", toDoItem.getDueDate());
                intent.putExtra("Time", toDoItem.getModifiedDate());
                intent.putExtra("Category", toDoItem.getCategory());
                intent.putExtra("Pathname", toDoItem.getPathname());

                intent.putExtra("Index", position);

                //startActivity(intent);
                startActivityForResult(intent, 1);

            }
        });



        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ToDoActivity.this);
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                alertBuilder.setNegativeButton("Cancel", null);
                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        ToDoItem toDoItem = (ToDoItem) allItems.get(position);
                        categories.get(catNumber).notes.remove(toDoItem);
                        allItems.remove(position);
                        deleteFile(toDoItem.getTaskName());
                        writeTodos();

                        //updateAllItems();
                        categoryAdapter.notifyDataSetChanged();

                        writeTodos();
                    }
                });
                alertBuilder.create().show();
                return true;
            }
        });



        // Floating button at the bottom
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ToDoActivity.this, ToDoDetailActivity.class);

                intent.putExtra("Title", "");
                intent.putExtra("Date", "");
                intent.putExtra("Time", "");
                intent.putExtra("Category", "");

                startActivityForResult(intent, 1);

            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            int index = data.getIntExtra("Index", -1);

            ToDoItem toDoItem = new ToDoItem(false, data.getStringExtra("Title"),
                    data.getStringExtra("Comment"), data.getStringExtra("Category"),
                    new Date(), new Date(), data.getStringExtra("Pathname"), data.getStringExtra("CategoryID"));

            Log.e(data.getStringExtra("ImagePath"), "*****");

            switch (data.getStringExtra("Category")){
                case "home":
                    catNumber = 0;
                    break;
                case "work":
                    catNumber = 1;
                    break;
                case "personal":
                    catNumber = 2;
                    break;
            }

            categories.get(catNumber).notes.add(toDoItem);
            writeTodos();
            updateAllItems();
            categoryAdapter.notifyDataSetChanged();

            writeTodos();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                toDoItemArrayAdapter.filter(searchQuery.toString().trim());
                ((BaseAdapter) toDoList.getAdapter()).notifyDataSetChanged();
                return true;
            }
        });



        return true;
    }


    private void setupToDo() {
        toDoArray = new ArrayList<>();

        File filesDir = this.getFilesDir();
        File todoFile = new File(filesDir + File.separator + filename);

        if (todoFile.exists()){
            readTodos(todoFile);
        } else{

            categories.add( new Category("Home", new ArrayList<ToDoItem>()));
            categories.add( new Category("Work", new ArrayList<ToDoItem>()));
            categories.add( new Category("Misc", new ArrayList<ToDoItem>()));

            for (int i = 0; i < categories.size(); i++){
                categories.get(i).notes.add(new ToDoItem (false, "Task Name 1", "Comment", "Category", new Date(), new Date(),"E/content://media/external/images/media/22", "CategoryID"));
                categories.get(i).notes.add(new ToDoItem (false, "Task Name 2", "Comment", "Category", new Date(), new Date(), "E/content://media/external/images/media/22", "CategoryID"));
                categories.get(i).notes.add(new ToDoItem (false, "Task Name 3", "Comment", "Category", new Date(), new Date(),"E/content://media/external/images/media/22", "CategoryID"));
            }
            writeTodos();
        }
    }


    private void readTodos(File todoFile) {
        FileInputStream inputStream = null;
        String todosText = "";
        try {
            inputStream = openFileInput(todoFile.getName());
            byte[] input = new byte[inputStream.available()];
            while (inputStream.read(input) != -1) {}
            todosText = new String(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Determine type of our collection
            Type collectionType = new TypeToken<List<Category>>(){}.getType();
            // Pull out our categories in a list
            List<Category> categoryList = gson.fromJson(todosText, collectionType);
            // Create a LinkedList that we can edit from our categories list and save it
            // to our global categories
            categories = new LinkedList(categoryList);

        }
    }

    private void writeTodos() {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            String json = gson.toJson(categories);
            byte[] bytes = json.getBytes();
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception ignored) {}
        }
    }

    private void updateAllItems(){
        allItems.clear();
        for (int i = 0; i < categories.size(); i++){
            allItems.add(categories.get(i).getName());
            for (int c = 0; c < categories.get(i).notes.size(); c++){
                allItems.add(categories.get(i).notes.get(c));
            }

        }
    }

}

