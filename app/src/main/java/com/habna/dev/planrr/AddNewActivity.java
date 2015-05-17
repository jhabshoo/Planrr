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
import android.widget.Spinner;

import com.parse.ParseObject;


public class AddNewActivity extends Activity {

  /**
   * enum to hold edittext validation state
   */
  private enum InputState {
    VALID,
    EMPTY,
    TOO_LONG
  }

  // input state for title field
  private InputState titleState = InputState.VALID;
  // input state for desc field
  private InputState descState = InputState.VALID;

  /**
   * input field max length values
   */
  private int MAX_TITLE_LENGTH = 30;
  private int MAX_DESC_LENGTH = 256;


  // TODO - fix style
  private final String TITLE_EMPTY_ERROR_MSG = "Title cannot be empty!";
  private final String DESC_EMPTY_ERROR_MSG = "Description cannot be empty.";
  private final String TITLE_TOO_LONG_ERROR_MSG = "Title cannot exceed length of " + MAX_TITLE_LENGTH +  ".";
  private final String DESC_TOO_LONG_ERROR_MSG = "Description cannot exceed length of " + MAX_TITLE_LENGTH +  ".";

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

    EditText titleText = (EditText) findViewById(R.id.titleEditText);
    EditText descText = (EditText) findViewById(R.id.descriptionEditText);

    validateTodo();

    if (!isInputErrors())  {
      saveTodo();
      Intent intent = new Intent(AddNewActivity.this, MainActivity.class);
      startActivity(intent);
    } else  {
      showErrorMessages();
    }

  }

  /**
   * Modifies titleError and descError
   */
  private void validateTodo() {
    EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
    EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);

    int titleLength = titleEditText.getText().length();
    int descLength = descriptionEditText.getText().length();

    if (titleLength <= 0) {
      titleState = InputState.EMPTY;
    } else if (titleLength > MAX_TITLE_LENGTH)  {
      titleState = InputState.TOO_LONG;
    }

    if (descLength <= 0)  {
      descState = InputState.EMPTY;
    } else if (descLength > MAX_DESC_LENGTH)  {
      descState = InputState.TOO_LONG;
    }

  }

  /**
   *
   * @return true if title or desc is invalid, false otherwise
   */
  private boolean isInputErrors() {
    return !titleState.equals(InputState.VALID) || !descState.equals(InputState.VALID);
  }

  /**
   * calls setError for invalid edittexts
   */
  void showErrorMessages()  {
    // title
    if (titleState.equals(InputState.EMPTY))  {
      ((EditText)findViewById(R.id.titleEditText)).setError(TITLE_EMPTY_ERROR_MSG);
    } else  {
      ((EditText)findViewById(R.id.titleEditText)).setError(TITLE_TOO_LONG_ERROR_MSG);
    }

    // desc
    if (descState.equals(InputState.EMPTY)) {
      ((EditText)findViewById(R.id.descriptionEditText)).setError(DESC_EMPTY_ERROR_MSG);
    } else  {
      ((EditText)findViewById(R.id.descriptionEditText)).setError(DESC_TOO_LONG_ERROR_MSG);
    }
  }

  /**
   * builds _TODO to be saved, calls saveTodo(String,String,String)
   */
  private void saveTodo() {
    EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
    EditText descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
    String depends = "";
    if (spinnerVisible == View.VISIBLE) {
      depends = ((ParseObject)((Spinner) findViewById(R.id.dependSpinner)).getSelectedItem()).getString(Todo.TITLE_KEY);
    }
    saveTodo(titleEditText.getText().toString(), descriptionEditText.getText().toString(), depends);
  }

  /**
   *
   * @param title title of newTodo
   * @param description description of newTodo
   * @param depends _TODO that newTodo depends on, set empty if not a dependent
   */
  private void saveTodo(String title, String description, String depends) {
    ParseObject newTodo = new ParseObject(Todo.OBJECT_KEY);
    newTodo.put(Todo.TITLE_KEY, title);
    newTodo.put(Todo.DESCRIPTION_KEY, description);
    newTodo.put(Todo.DEPENDS_KEY, depends);
    newTodo.saveInBackground();
  }

}

