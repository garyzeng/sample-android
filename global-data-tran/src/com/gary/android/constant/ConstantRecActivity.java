package com.gary.android.constant;

import com.gary.android.global.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ConstantRecActivity extends Activity {

	private TextView tx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constant_rec);
		
		tx = (TextView)findViewById(R.id.tv_rec);
		
		tx.setText(Constant.name);
		
	}
}
