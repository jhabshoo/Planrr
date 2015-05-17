package com.habna.dev.planrr;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by jhabshoosh on 5/17/15.
 */
public class PlanrrApplication extends Application {

  private final String APPLICATION_ID = "UKR4OrXmDwTwqUPLLg0gyhfZc2ot0HeDhYzZU22W";
  private final String CLIENT_KEY="ypIocsTxxeBSBahikGl2Gplj84vMW3QTWh9JrvNz";

  @Override
  public void onCreate() {
    super.onCreate();

    Parse.enableLocalDatastore(this);
    Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
  }
}
