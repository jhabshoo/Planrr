package com.habna.dev.planrr;

import android.app.Activity;
import android.app.Application;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;


public class MainActivity extends ListActivity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Add new button
    Button addButton = (Button) findViewById(R.id.addButton);
    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // goto AddTodoActivity
        Intent intent = new Intent(MainActivity.this, AddNewActivity.class);
        startActivity(intent);
      }
    });

    // populate list
    ParseQueryAdapter todoListAdapter = Todo.getQueryAdapter(this);
    todoListAdapter.setTextKey(Todo.TITLE_KEY);
    setListAdapter(todoListAdapter);

    getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setVisibility(1);
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
