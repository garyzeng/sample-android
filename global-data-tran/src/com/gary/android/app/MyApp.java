package com.gary.android.app;

import android.app.Application;
import android.util.Log;

public class MyApp extends Application {
	
	private String name;
	
	@Override
	public void onCreate() {
		super.onCreate();
		name = "这是默认值";
		Log.i("MyApp.onCreate", name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
