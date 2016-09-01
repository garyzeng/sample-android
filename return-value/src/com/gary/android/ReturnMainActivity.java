package com.gary.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ReturnMainActivity extends Activity {
	
	private Button btn;
	
	private TextView tx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_return_main);
		
		btn = (Button)findViewById(R.id.cal);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ReturnMainActivity.this, ResultActivity.class);
				int first = Integer.valueOf(((EditText)findViewById(R.id.first)).getText().toString());
				int second = Integer.valueOf(((EditText)findViewById(R.id.second)).getText().toString());
				intent.putExtra("first", first);
				intent.putExtra("second", second);
				startActivityForResult(intent, 1);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK && requestCode == 1) {
			tx = (TextView)findViewById(R.id.result);
			tx.setText(data.getIntExtra("result", 0) + "");
		}
	}
}
