package com.gary.android.intent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ReactActivity extends Activity {
	
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_react);
		tv = (TextView)findViewById(R.id.tv);
		
		tv.setText(getIntent().getStringExtra("now"));
	}
	
	// below methods to test lifecycle.
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.i("ReactActivity", "onDestroy");
	}

	// click back, this activity goes to onDestroy.
	// so if click "now" button on another activity to
	// launch this one, a new instance is created, new hash code
	@Override
	protected void onResume() {
		super.onResume();
		
		Log.i("ReactActivity", "onResume--" + hashCode());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		Log.i("ReactActivity", "onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		
		Log.i("ReactActivity", "onRestart");
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		Log.i("ReactActivity", "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		Log.i("ReactActivity", "onStop");
	}
	
	
}
