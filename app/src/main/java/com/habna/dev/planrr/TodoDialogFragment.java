package com.habna.dev.planrr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by jhabshoosh on 5/17/15.
 */
public class TodoDialogFragment extends DialogFragment {

  private Todo todo;

  public TodoDialogFragment(Todo todo)  {
    super();
    this.todo = todo;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(todo.toString());
    return builder.create();
  }
}
