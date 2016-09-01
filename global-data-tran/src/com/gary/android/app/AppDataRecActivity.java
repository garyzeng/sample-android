package com.gary.android.app;

import com.gary.android.constant.ConstantMainActivity;
import com.gary.android.global.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AppDataRecActivity extends Activity {

	private Button btn;
	private Button btnCon;
	private TextView tx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_data_rec);
		
		tx = (TextView)findViewById(R.id.tv_rec);
		
		MyApp app = (MyApp)getApplication();
		tx.setText(app.getName());
		
		registerClick();
		
		Log.i("AppDataRecActivity", "onCreate---" + hashCode());
	}
	
	
	private void registerClick() {
		btn = (Button)findViewById(R.id.btn_rec);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				MyApp app = (MyApp)getApplication();
				app.setName("from AppDataRecActivity");
				
				Intent intent = new Intent(AppDataRecActivity.this, AppDataTranMainActivity.class);
				// start a new AppDataTranMainActivity instance.
				// this activity instance is pushed into stack
				startActivity(intent);
				
			}
		});
		
		btnCon = (Button)findViewById(R.id.btn_new);
		btnCon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AppDataRecActivity.this, ConstantMainActivity.class);
				startActivity(intent);
				
			}
		});
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		Log.i("AppDataRecActivity", "onStop---" + hashCode());
	}
	
	// press back, the top instance in the stack is destroyed
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		Log.i("AppDataRecActivity", "onDestroy---" + hashCode());
	}
}
