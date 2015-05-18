package com.habna.dev.planrr;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class AddNewActivity extends ListActivity {

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

    CheckedTextView item = (CheckedTextView) v;
    item.setChecked(!item.isChecked());
    if (item.isChecked()) {
      dependsList.add(item.getText().toString());
    } else  {
      dependsList.remove(item.getText().toString());
    }
  }

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


  private List<String> dependsList = new ArrayList<>();
  private List<ListItem> todosList = new ArrayList<>();

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

  private int todosListVisible = View.GONE;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_new);
    getActionBar().setDisplayHomeAsUpEnabled(true);


    // save button
    Button saveButton = (Button) findViewById(R.id.saveButton);
    saveButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addNewTodo();
      }
    });

    // hide spinner when unchecked
    final CheckBox dependCheckBox = (CheckBox) findViewById(R.id.dependCheckBox);
    dependCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        todosListVisible = todosListVisible == View.GONE ? View.VISIBLE : View.GONE;
        getListView().setVisibility(todosListVisible);
      }
    });

    ListView listView = getListView();
    ParseQuery query = new ParseQuery(Todo.OBJECT_KEY);
    List results = null;
    try {
      results = query.find();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    for (Object obj : results)  {
      ParseObject result = (ParseObject) obj;
      String title = result.getString(Todo.TITLE_KEY);
      ListItem listItem = new ListItem(title);
      todosList.add(listItem);
    }

    ArrayAdapter adapter = new ArrayAdapter<ListItem>(this,
        android.R.layout.simple_list_item_multiple_choice, todosList);

    getListView().setVisibility(todosListVisible);
    getListView().setAdapter(adapter);
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

  /**
   * manages adding newTodo flow
   */
  private void addNewTodo() {
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
    saveTodo(titleEditText.getText().toString(), descriptionEditText.getText().toString());
  }

  /**
   *
   * @param title title of newTodo
   * @param description description of newTodo\
   */
  private void saveTodo(String title, String description) {
    ParseObject newTodo = new ParseObject(Todo.OBJECT_KEY);
    newTodo.put(Todo.TITLE_KEY, title);
    newTodo.put(Todo.DESCRIPTION_KEY, description);
    newTodo.put(Todo.DEPENDS_KEY, dependsList);
    newTodo.saveInBackground();
  }

  private class ListItem  {
    private String id;

    public ListItem(String str) {
      id = str;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return id;
    }
  }


}

