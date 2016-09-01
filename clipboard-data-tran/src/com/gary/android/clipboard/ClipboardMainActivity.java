package com.gary.android.clipboard;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClipboardMainActivity extends Activity {
	
	private Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clipboard_main);
		
		btn = (Button)findViewById(R.id.btn);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
				clip.setText("from clipboard");
				
				Intent intent = new Intent(ClipboardMainActivity.this, DataRecActivity.class);
				
				startActivity(intent);
				
			}
		});
	}
}
