package com.habna.dev.planrr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;


public class AddNewActivity extends Activity {

  private int spinnerVisible = View.GONE;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_new);

    // depends spinner
    ParseSpinnerAdapter dependsSpinnerAdapter = new ParseSpinnerAdapter(this, Todo.OBJECT_KEY);
    dependsSpinnerAdapter.setTextKey(Todo.TITLE_KEY);
    final Spinner dependSpinner = (Spinner) findViewById(R.id.dependSpinner);
    dependSpinner.setAdapter(dependsSpinnerAdapter);
    dependSpinner.setVisibility(spinnerVisible);

    // hide spinner when unchecked
    final CheckBox dependCheckBox = (CheckBox) findViewById(R.id.dependCheckBox);
    dependCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        spinnerVisible = spinnerVisible == View.GONE ? View.VISIBLE : View.GONE;
        dependSpinner.setVisibility(spinnerVisible);
      }
    });


    // save button
    Button saveButton = (Button) findViewById(R.id.saveButton);
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addNewTodo();
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_add_new, menu);
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

  private void addNewTodo() {
    if (validateTodo()) {
      saveTodo();
      Intent intent = new Intent(AddNewActivity.this, MainActivity.class);
      startActivity(intent);
    } else  {
      Toast.makeText(AddNewActivity.this, "Fill in all fields!", Toast.LENGTH_LONG);
      clearForm();
    }
  }

  private boolean validateTodo() {
    EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
    EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

    if (titleEditText.getText().length() == 0 || descriptionEditText.getText().length() == 0) {
      return false;
    }
    return true;
  }

  private void saveTodo() {
    EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
    EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
    String depends = "";
    if (spinnerVisible == View.VISIBLE) {
      depends = ((ParseObject)((Spinner) findViewById(R.id.dependSpinner)).getSelectedItem()).getString(Todo.TITLE_KEY);
    }
    saveTodo(titleEditText.getText().toString(), descriptionEditText.getText().toString(), depends);
  }

  private void saveTodo(String title, String description, String depends) {
    ParseObject newTodo = new ParseObject(Todo.OBJECT_KEY);
    newTodo.put(Todo.TITLE_KEY, title);
    newTodo.put(Todo.DESCRIPTION_KEY, description);
    newTodo.put(Todo.DEPENDS_KEY, depends);
    newTodo.saveInBackground();
  }

  private void clearForm()  {
    EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
    EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
    titleEditText.setText("");
    descriptionEditText.setText("");
  }

}
