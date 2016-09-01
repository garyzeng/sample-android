package com.gary.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ResultActivity extends Activity {
	
	private TextView first;
	
	private TextView second;
	
	private Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		
		first = (TextView)findViewById(R.id.first);
		second = (TextView)findViewById(R.id.second);
		btn = (Button)findViewById(R.id.back);
		
		int fv = getIntent().getIntExtra("first", 0);
		int sv = getIntent().getIntExtra("second", 0);
		
		first.setText(fv + "");
		second.setText(sv + "");
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				int result = Integer.valueOf(((EditText)findViewById(R.id.result)).getText().toString());
				intent.putExtra("result", result);
				setResult(RESULT_OK, intent);
				finish();
			}
			
		});
	}
	
	
}
