package com.gary.android.constant;

import com.gary.android.global.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConstantMainActivity extends Activity {

	private Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constant_main);
		
		btn = (Button)findViewById(R.id.btn_main);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Constant.name = "from ConstantMainActivity";
				
				Intent intent = new Intent(ConstantMainActivity.this, ConstantRecActivity.class);
				// start a new AppDataRecActivity instance.
				// this activity instance is pushed into stack
				startActivity(intent);
				
			}
		});
		
	}
}
