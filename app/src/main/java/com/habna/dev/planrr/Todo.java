package com.habna.dev.planrr;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;

/**
 * Created by jhabshoosh on 5/16/15.
 */
public class Todo extends ParseObject {

  public static final String TITLE_KEY = "title";
  public static final String DESCRIPTION_KEY = "description";
  public static final String DEPENDS_KEY = "depends";
  public static final String OBJECT_KEY = "Todo";

  private String title;
  private String description;
  private String depends;

  public Todo(String title, String description, String depends) {
    this.title = title;
    this.description = description;
    this.depends = depends;
  }

  public static ParseQueryAdapter<ParseObject> getQueryAdapter(Context context)  {
    return new ParseQueryAdapter<ParseObject>(context, Todo.OBJECT_KEY);
  }

  @Override
  public String toString() {
    return "Todo{" +
        "title='" + title + '\'' +
        ", description='" + description + '\'' +
        (depends != "" ? (", depends='" + depends + '\'') : "") +
        '}';
  }
}
