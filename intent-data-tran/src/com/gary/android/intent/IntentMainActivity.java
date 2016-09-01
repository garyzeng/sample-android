package com.gary.android.intent;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class IntentMainActivity extends Activity {
	
	private Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_main);
		
		btn = (Button)findViewById(R.id.button);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(IntentMainActivity.this, ReactActivity.class);
				intent.putExtra("now", new Date().toLocaleString());
				startActivity(intent);
			}
		});
	}
	
	
	// below methods to test lifecycle.
	// click "now" button, this activity goes to onStop,
	// another activity goes to onResume.
	// click back, another activity goes to onDestroy,
	// this one goes to onResume
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.i("IntentMainActivity", "onDestroy");
	}

	// hashcode tells that only one instance servers if this
	// activity is not destroyed.
	@Override
	protected void onResume() {
		super.onResume();
		
		Log.i("IntentMainActivity", "onResume--" + hashCode());
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		Log.i("IntentMainActivity", "onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		
		Log.i("IntentMainActivity", "onRestart");
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		Log.i("IntentMainActivity", "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		Log.i("IntentMainActivity", "onStop");
	}
}
