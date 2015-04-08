package com.ytzys.servicemutirequestsdemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private TextView textView;
	private Button button1;
	private Button button2;
	public static int initValue = 8;
	int programId = initValue;
	int subId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initUI();
		IntentFilter filter = new IntentFilter();
		filter.addAction("finish");
		registerReceiver(receiver, filter);
	}

	private void initUI() {
		textView = (TextView) findViewById(R.id.text);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button1.setText("program 1");
		button2.setText("program 2");
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button1:
			textView.setText("button1 clicked");
			subId = 1;
			stopMutiRequestService();
			startMutiRequestService(programId, subId);
			break;
		case R.id.button2:
			textView.setText("button2 clicked");
			subId = 2;
			stopMutiRequestService();
			startMutiRequestService(programId, subId);
			break;
		default:
			break;

		}

	}

	private void stopMutiRequestService() {
		Intent intent = new Intent(this, MutiRequestsService.class);
		intent.putExtra("action", MutiRequestsService.STOP_SERVICE);
		startService(intent);
	}

	private void startMutiRequestService(int programId, int subId) {
		Intent intent = new Intent(this, MutiRequestsService.class);
		intent.putExtra("action", MutiRequestsService.START_SERVICE);
		intent.putExtra("programId", programId);
		intent.putExtra("subId", subId);
		startService(intent);
	}

	@Override
	protected void onDestroy() {
		stopMutiRequestService();
		super.onDestroy();
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("finish")) {
				if (intent.getIntExtra("subId", 0) == 1) {
					textView.setText("serve program 1");
				} else {
					textView.setText("serve program 2");

				}
			}

		}
	};

}
