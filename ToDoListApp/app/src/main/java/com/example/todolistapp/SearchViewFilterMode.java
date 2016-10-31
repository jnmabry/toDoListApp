package com.example.todolistapp;

/**
 * Created by JoshuaMabry on 10/27/16.
 */

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

/**
 * Shows a list that can be filtered in-place with a SearchView in non-iconified mode.
 */
public class SearchViewFilterMode extends Activity implements SearchView.OnQueryTextListener {

    private SearchView mSearchView;
    private ListView mListView;
    public ArrayList<ToDoItem> toDoArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.todo_list_item);

        mSearchView = (SearchView) findViewById(R.id.search);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ToDoItemArrayAdapter(this, R.layout.todo_list_item, toDoArray));
        mListView.setTextFilterEnabled(true);
        setupSearchView();
    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
