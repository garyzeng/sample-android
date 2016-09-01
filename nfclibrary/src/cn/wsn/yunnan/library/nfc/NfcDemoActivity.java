package cn.wsn.yunnan.library.nfc;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NfcDemoActivity extends Activity {
	/** Called when the activity is first created. */
	private Timer checkConnection;

	

	private Button btnGetCardCode;
	private Button btnGetMultiple;
	private Button btnUpdateCardCode;
	private Button btnUpdateAll;

	private Button btnDeleteCustomerManagerFeedback;
	private Button btnAppendCustomerManagerFeedback;
	private Button btnUpdateCustomerManagerFeedback;

	private Button btnDeleteMonopolyFeedback;
	private Button btnAppendMonopolyFeedback;
	private Button btnUpdateMonopolyFeedback;

	private Button btnDeleteDeliveryFeedback;
	private Button btnAppendDeliveryFeedback;
	private Button btnUpdateDeliveryFeedback;

	private Button btnGetIsMain;
	private Button btnUpdateIsMain;

	private Button btnUpdateCardType;
	private Button btnGetCardType;

	private EditText txtCardType;
	private EditText txtCardCode;
	private EditText txtShopName;
	private EditText txtRelibility;
	private EditText txtCustomerManagerFeedback;
	private EditText txtDeliveryFeedback;
	private EditText txtMonopolyFeedback;
	private EditText txtIsMain;
	private EditText txtCustomerManagerFeedbackFlag;
	private EditText txtDeliveryFeedbackFlag;
	private EditText txtMonopolyFeedbackFlag;
	
	private TextView lblConnection;
	private NfcAdapter mAdapter;
	private String[][] techList;
	private IntentFilter[] intentFilters;
	private PendingIntent pendingIntent;
	private Tag tag;
	private INfcProvider nfcProvider;

	// private Application
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// String uuidString = "58CF4715-E416-4430-BE32-F4606C19CE01";
		// byte[] ddd = EncodingUtils.getBytes(uuidString, "utf-8");
		//
		btnGetCardCode = (Button) this.findViewById(R.id.btnGetCardCode);
		btnUpdateCardCode = (Button) this
				.findViewById(R.id.btnUpdateCardCode);

		btnDeleteCustomerManagerFeedback = (Button) this
				.findViewById(R.id.btnDeleteCustomerManagerFeedback);
		btnAppendCustomerManagerFeedback = (Button) this
				.findViewById(R.id.btnAppendCustomerManagerFeedback);
		btnUpdateCustomerManagerFeedback = (Button) this
				.findViewById(R.id.btnUpdateCustomerManagerFeedback);

		btnDeleteMonopolyFeedback = (Button) this
				.findViewById(R.id.btnDeleteMonopolyFeedback);
		btnAppendMonopolyFeedback = (Button) this
				.findViewById(R.id.btnAppendMonopolyFeedback);
		btnUpdateMonopolyFeedback = (Button) this
				.findViewById(R.id.btnUpdateMonopolyFeedback);

		btnDeleteDeliveryFeedback = (Button) this
				.findViewById(R.id.btnDeleteDeliveryFeedback);
		btnAppendDeliveryFeedback = (Button) this
				.findViewById(R.id.btnAppendDeliveryFeedback);
		btnUpdateDeliveryFeedback = (Button) this
				.findViewById(R.id.btnUpdateDeliveryFeedback);

		btnGetIsMain = (Button) this.findViewById(R.id.btnGetIsMain);

		btnUpdateIsMain = (Button) this.findViewById(R.id.btnUpdateIsMain);

		btnGetMultiple = (Button) this.findViewById(R.id.btnGetMultiple);
		btnUpdateAll = (Button) this.findViewById(R.id.btnUpdateAll);
		btnUpdateCardType = (Button) this.findViewById(R.id.btnUpdateCardType);
		btnGetCardType = (Button) this.findViewById(R.id.btnGetCardType);

		lblConnection = (TextView) this.findViewById(R.id.lblConnection);

		txtCardType = (EditText) this.findViewById(R.id.txtCardType);
		txtCardCode = (EditText) this.findViewById(R.id.txtCardCode);
		txtShopName = (EditText) this.findViewById(R.id.txtShopName);
		txtRelibility = (EditText) this.findViewById(R.id.txtRelibility);
		txtIsMain = (EditText) this.findViewById(R.id.txtIsMain);
		
		txtCustomerManagerFeedback = (EditText) this
				.findViewById(R.id.txtCustomerManagerFeedback);
		txtDeliveryFeedback = (EditText) this
				.findViewById(R.id.txtDeliveryFeedback);
		txtMonopolyFeedback = (EditText) this
				.findViewById(R.id.txtMonopolyFeedback);
		
		txtCustomerManagerFeedbackFlag = (EditText) this
				.findViewById(R.id.txtCustomerManagerFeedbackFlag);
		txtDeliveryFeedbackFlag = (EditText) this
				.findViewById(R.id.txtDeliveryFeedbackFlag);
		txtMonopolyFeedbackFlag = (EditText) this
				.findViewById(R.id.txtMonopolyFeedbackFlag);
		
		//获取nfc适配器
		mAdapter = NfcAdapter.getDefaultAdapter(this);
        //定义程序可以兼容的nfc协议，例子为nfca和nfcv
		//在Intent filters里声明你想要处理的Intent，一个tag被检测到时先检查前台发布系统，
		//如果前台Activity符合Intent filter的要求，那么前台的Activity的将处理此Intent。
		//如果不符合，前台发布系统将Intent转到Intent发布系统。如果指定了null的Intent filters，
		//当任意tag被检测到时，你将收到TAG_DISCOVERED intent。因此请注意你应该只处理你想要的Intent。
		techList = new String[][] {
				new String[] { android.nfc.tech.NfcV.class.getName() },
				new String[] { android.nfc.tech.NfcA.class.getName() } };
		intentFilters = new IntentFilter[] { new IntentFilter(
				NfcAdapter.ACTION_TECH_DISCOVERED), };
		//创建一个 PendingIntent 对象, 这样Android系统就能在一个tag被检测到时定位到这个对象
		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		nfcProvider = new NfcProvider(this);
		try {
			nfcProvider.Initialize();
		} catch (NfcException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Initialize Error", 2000)
					.show();
		}

		btnGetCardCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GetCardCode();
			}
		});

		btnUpdateCardCode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateCardCode();
			}
		});
		btnUpdateCardType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateCardType();
			}

		});
		btnGetCardType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GetCardType();
			}

		});
		
		btnAppendCustomerManagerFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						AppendCustomerManagerFeedback();
					}
				});

		btnDeleteCustomerManagerFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						DeleteCustomerManagerFeedback();
					}
				});

		btnUpdateCustomerManagerFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						UpdateCustomerManagerFeedback();
					}
				});

		btnAppendMonopolyFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						AppendMonopolyFeedback();
					}
				});

		btnDeleteMonopolyFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						DeleteMonopolyFeedback();
					}
				});

		btnUpdateMonopolyFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						UpdateMonopolyFeedback();
					}
				});

		btnAppendDeliveryFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						AppendDeliveryFeedback();
					}
				});

		btnDeleteDeliveryFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						DeleteDeliveryFeedback();
					}
				});

		btnUpdateDeliveryFeedback
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						UpdateDeliveryFeedback();
					}
				});

		btnGetMultiple.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GetMultiple();
			}
		});

		btnUpdateAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateAll();
			}
		});
		
		
		btnGetIsMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GetIsMain();
			}
		});
		
		btnUpdateIsMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdateIsMain();
			}
		});
	}

	private void timerMethod() {
		this.runOnUiThread(timer_Tick);
	}

	private Runnable timer_Tick = new Runnable() {
		@Override
		public void run() {
			try {
				if (tag != null) {
					Boolean isOnline = nfcProvider.isOnline(tag);
					if (isOnline)// Block 0x0000 was read !
					{
						lblConnection.setText("Connected");
					} else {
						lblConnection.setText("Not Connected");
					}
				} else {
					lblConnection.setText("Not Connected");
				}
			} catch (Exception e) {
				lblConnection.setText("Not Connected");
			}
		}
	};

	public void onNewIntent(Intent intent) {
		tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		
		return;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
 
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//使用前台发布系统
		mAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters,
				techList);

		if (checkConnection == null) {
			checkConnection = new Timer();
			checkConnection.schedule(new TimerTask() {
				@Override
				public void run() {
					timerMethod();
				}
			}, 0, 800);
		}
	}

	private void GetCardType() {
		if (tag != null) {
			try {
				Object[] result = nfcProvider.getItems(tag,
						new String[] { "CardType" });

				txtCardType.setText(result[0].toString());

			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "View Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}

	private void GetMultiple() {
		if (tag != null) {
			try {

				Object[] result = nfcProvider.getItems(tag, new String[] {
						"CardType", "CardCode", "ShopName", "Relibility",
						"CustomerManagerFeedback","SalerFeedback","DispacterFeedback",
						"CustomerManagerFeedbackFlag","SalerFeedbackFlag","DispacterFeedbackFlag"});

				txtCardType.setText(result[0].toString());
				txtCardCode.setText(result[1].toString());
				txtShopName.setText(result[2].toString());
				txtRelibility.setText(result[3].toString());
				txtCustomerManagerFeedback.setText(result[4].toString());
				txtMonopolyFeedback.setText(result[5].toString());
				txtDeliveryFeedback.setText(result[6].toString());
				
				txtCustomerManagerFeedbackFlag.setText(result[7].toString());
				txtMonopolyFeedbackFlag.setText(result[8].toString());
				txtDeliveryFeedbackFlag.setText(result[9].toString());

			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "View Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}

	}

	private void GetCardCode() {
		if (tag != null) {

			Integer cardCode = 0;

			try {
				cardCode = nfcProvider.<Integer> getItem(tag, "CardCode");

				txtCardCode.setText(cardCode.toString());

			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "View Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}

	}

	private void UpdateAll() {
		if (tag != null) {

			Integer cardCode = Integer.parseInt(txtCardCode.getText().toString());
			String customerManagerFeedback = txtCustomerManagerFeedback
					.getText().toString();
			String shopName = txtShopName.getText().toString();
			Integer relibility = txtRelibility.getText().toString() == "" ? 0
					: Integer.parseInt(txtRelibility.getText().toString());

			try {
				nfcProvider.setItem(tag, "CardCode", cardCode);
				nfcProvider.setItem(tag, "ShopName", shopName);
				nfcProvider.setItem(tag, "CustomerManagerFeedback",
						customerManagerFeedback);
				nfcProvider.setItem(tag, "Relibility", relibility);
				Toast.makeText(getApplicationContext(), "Update Successfully.",
						2000).show();
			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Update Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}

	private void UpdateCardCode() {
		if (tag != null) {
			Integer cardCode = Integer.parseInt(txtCardCode.getText().toString());

			try {
				nfcProvider.setItem(tag, "CardCode", cardCode);
				Toast.makeText(getApplicationContext(), "Update Successfully.",
						2000).show();
			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Update Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}

	private void UpdateCardType() {
		// TODO Auto-generated method stub
		if (tag != null) {

			Integer cardType = txtCardType.getText().toString() == "" ? 1
					: Integer.parseInt(txtCardType.getText().toString());
			try {
				nfcProvider.setItem(tag, "CardType", cardType);
				Toast.makeText(getApplicationContext(), "Update Successfully.",
						2000).show();
			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Update Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}
	
	private void UpdateIsMain() {
		// TODO Auto-generated method stub
		if (tag != null) {

			Integer isMain = txtIsMain.getText().toString() == "" ? 1
					: Integer.parseInt(txtIsMain.getText().toString());
			try {
				nfcProvider.setItem(tag, "IsMain", isMain);
				Toast.makeText(getApplicationContext(), "Update Successfully.",
						2000).show();
			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Update Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}
	
	private void GetIsMain() {
		if (tag != null) {
			try {
				Object[] result = nfcProvider.getItems(tag,
						new String[] { "IsMain" });

				txtIsMain.setText(result[0].toString());

			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "View Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}

	private void AppendCustomerManagerFeedback() {
		if (tag != null) {

			String customerManagerFeedback = txtCustomerManagerFeedback
					.getText().toString();

			try {
				nfcProvider.appendStringItem(tag, "CustomerManagerFeedback",
						customerManagerFeedback);

				Toast.makeText(getApplicationContext(), "Update Successfully.",
						2000).show();
			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Update Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}

	private void DeleteCustomerManagerFeedback() {
		if (tag != null) {

			try {
				nfcProvider.clearItem(tag, "CustomerManagerFeedback");

				Toast.makeText(getApplicationContext(), "Delete Successfully.",
						2000).show();
			} catch (NfcException e) {
				Toast.makeText(getApplicationContext(), "Delete Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}
	
	private void UpdateCustomerManagerFeedback() {
		if (tag != null) {

			try {
				String customerManagerFeedback = txtCustomerManagerFeedback.getText().toString();
				nfcProvider.setItem(tag, "CustomerManagerFeedback", customerManagerFeedback);
				nfcProvider.clearItem(tag, "CustomerManagerFeedback");

				Toast.makeText(getApplicationContext(), "Delete Successfully.",
						2000).show();
			} catch (NfcException e) {
				Toast.makeText(getApplicationContext(), "Delete Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}

	private void AppendMonopolyFeedback() {
		if (tag != null) {

			String monopolyFeedback = txtMonopolyFeedback.getText().toString();

			try {
				nfcProvider.appendStringItem(tag, "SalerFeedback",
						monopolyFeedback);

				Toast.makeText(getApplicationContext(), "Update Successfully.",
						2000).show();
			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Update Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}

	private void DeleteMonopolyFeedback() {
		if (tag != null) {

			try {
				nfcProvider.clearItem(tag, "SalerFeedback");

				Toast.makeText(getApplicationContext(), "Delete Successfully.",
						2000).show();
			} catch (NfcException e) {
				Toast.makeText(getApplicationContext(), "Delete Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}
	
	private void UpdateMonopolyFeedback() {
		if (tag != null) {

			try {
				String monopolyFeedback = txtMonopolyFeedback.getText().toString();
				nfcProvider.setItem(tag, "SalerFeedback", monopolyFeedback);
				nfcProvider.clearItem(tag, "SalerFeedback");

				Toast.makeText(getApplicationContext(), "Delete Successfully.",
						2000).show();
			} catch (NfcException e) {
				Toast.makeText(getApplicationContext(), "Delete Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}
	
	private void AppendDeliveryFeedback() {
		if (tag != null) {

			String deliveryManagerFeedback = txtDeliveryFeedback.getText().toString();

			try {
				nfcProvider.appendStringItem(tag, "DispacterFeedback",
						deliveryManagerFeedback);

				Toast.makeText(getApplicationContext(), "Update Successfully.",
						2000).show();
			} catch (NfcException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(), "Update Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}

	private void DeleteDeliveryFeedback() {
		if (tag != null) {

			try {
				nfcProvider.clearItem(tag, "DispacterFeedback");

				Toast.makeText(getApplicationContext(), "Delete Successfully.",
						2000).show();
			} catch (NfcException e) {
				Toast.makeText(getApplicationContext(), "Delete Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}
	
	private void UpdateDeliveryFeedback() {
		if (tag != null) {

			try {
				String deliveryManagerFeedback = txtDeliveryFeedback.getText().toString();
				nfcProvider.setItem(tag, "DispacterFeedback", deliveryManagerFeedback);
				nfcProvider.clearItem(tag, "DispacterFeedback");

				Toast.makeText(getApplicationContext(), "Delete Successfully.",
						2000).show();
			} catch (NfcException e) {
				Toast.makeText(getApplicationContext(), "Delete Error:" + e.getMessage(), 2000)
						.show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Not Connected", 2000)
					.show();
		}
	}
}