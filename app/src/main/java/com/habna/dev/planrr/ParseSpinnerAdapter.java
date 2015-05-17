package com.habna.dev.planrr;

import android.content.Context;
import android.widget.SpinnerAdapter;

import com.parse.ParseQueryAdapter;

/**
 * Created by jhabshoosh on 5/17/15.
 */
public class ParseSpinnerAdapter extends ParseQueryAdapter implements SpinnerAdapter {

  ParseSpinnerAdapter(Context context, String className) {
    super(context, className);
  }

  @Override
  public int getViewTypeCount() {
    return 1;
  }


}
