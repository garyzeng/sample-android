package com.gary.android.app;

import com.gary.android.global.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AppDataTranMainActivity extends Activity {

	private Button btn;
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_data_tran_main);
		
		
		tv = (TextView)findViewById(R.id.tv_main);
		final MyApp app = (MyApp)getApplication();
		tv.setText(app.getName());
		
		
		btn = (Button)findViewById(R.id.btn_main);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				app.setName("from AppDataTranMainActivity");
				
				Intent intent = new Intent(AppDataTranMainActivity.this, AppDataRecActivity.class);
				// start a new AppDataRecActivity instance.
				// this activity instance is pushed into stack
				startActivity(intent);
				
			}
		});
		
		Log.i("AppDataTranMainActivity", "onCreate---" + hashCode());
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		Log.i("AppDataTranMainActivity", "onStop---" + hashCode());
	}
	
	// press back, the top instance in the stack is destroyed
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.i("AppDataTranMainActivity", "onDestroy---" + hashCode());
	}
	
}
