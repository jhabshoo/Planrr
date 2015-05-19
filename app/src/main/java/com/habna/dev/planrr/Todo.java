package com.habna.dev.planrr;

import android.content.Context;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhabshoosh on 5/16/15.
 */
public @ParseClassName("Todo") class Todo extends ParseObject {

  public static final String TITLE_KEY = "title";
  public static final String DESCRIPTION_KEY = "description";
  public static final String DEPENDS_KEY = "depends";
  public static final String OBJECT_KEY = "Todo";
  public static final String ID_KEY = "objectId";

  private String title;
  private String description;
  private ArrayList<String> depends = new ArrayList<>();
  private String id;

  public Todo() {

  }

  public static ParseQueryAdapter<ParseObject> getQueryAdapter(Context context)  {
    return new ParseQueryAdapter<ParseObject>(context, Todo.OBJECT_KEY);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title, boolean commit) {
    if (commit) {
      put(TITLE_KEY, title);
    }
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description, boolean commit) {
    if (commit) {
      put(DESCRIPTION_KEY, description);
    }
    this.description = description;
  }

  public ArrayList<String> getDepends() {
    return depends;
  }

  public void setDepends(ArrayList<String> depends, boolean commit) {
    if (commit) {
      put(DEPENDS_KEY, depends);
    }
    this.depends = depends;
  }

  public void loadDependIds(List<String> dependTitles)  {
    for (String title : dependTitles) {
      ParseQuery<Todo> query = ParseQuery.getQuery("Todo");
      query.whereEqualTo(TITLE_KEY, title);
      try {
        ParseObject object = query.getFirst();
        String id = object.getObjectId();
        depends.add(id);
        Log.d("test","test");
      } catch (ParseException pe) {

      }
    }
    put(DEPENDS_KEY, depends == null ? new ArrayList<String>() : depends);
  }

  private void setId(String str)  {
    id = str;
  }

  private String getId()  {
    return id;
  }

  @Override
  public String toString() {
    return "Todo{" +
        "title='" + title + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
