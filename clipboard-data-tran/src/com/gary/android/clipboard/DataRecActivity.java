package com.gary.android.clipboard;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class DataRecActivity extends Activity {
	
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_rec);
		
		tv = (TextView)findViewById(R.id.tv);
		
		ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		
		tv.setText(clip.getText().toString());
		
	}
}
