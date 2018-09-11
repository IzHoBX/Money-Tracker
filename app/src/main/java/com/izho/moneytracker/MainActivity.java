package com.izho.moneytracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private final String EXTRA_CATEGORIES_KEY = "extra categories";
    private final String CREATE_NEW_CATEGORY = "CREATE NEW";
    private String currentSelectedCategory;

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_new_entry:
                    setUpItemNewEntry();
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_options:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.navigation_new_entry);

    }

    private void setUpItemNewEntry() {

        final Set<String> defaultCategories = new HashSet<String>(Arrays.asList(getResources().getStringArray(R.array.categories)));

        SharedPreferences sp = this.getSharedPreferences("com.izho.moneytracker", Context.MODE_PRIVATE);
        Set<String> extraCategories = new HashSet<String>(sp.getStringSet(EXTRA_CATEGORIES_KEY, new HashSet<String>()));

        //reasonable bound of 100
        List<String> allCategories = new LinkedList<>();
        for(String s: defaultCategories)
            allCategories.add(s);
        for(String s: extraCategories)
            allCategories.add(s);
        allCategories.add(CREATE_NEW_CATEGORY);

        Spinner categoriesDropDown = findViewById(R.id.categories_drop_down_list);

        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, allCategories);
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categoriesDropDown.setAdapter(dropDownAdapter);

        categoriesDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSelectedCategory = ((TextView) view).getText() + "";
                if(currentSelectedCategory.equals(CREATE_NEW_CATEGORY)) {
                    promptToCreateNewCategory();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void promptToCreateNewCategory() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(R.layout.prompt_create_new_category)
                .setTitle("Create New Expenditure Category")
                .setPositiveButton("Done", null)
                .setNegativeButton("Cancel", null)
                .create();
        alertDialog.setButton(-1, "Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryNameInput = ((EditText) alertDialog.findViewById(R.id.categoryNameInput)).getText().toString();
                if(categoryNameInput.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Category name must not be empty", Toast.LENGTH_SHORT).show();
                    promptToCreateNewCategory();
                    return;
                }
                Toast.makeText(MainActivity.this, categoryNameInput + "", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

}
