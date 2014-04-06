package com.codedemigod.ussdinterceptor;

import com.codedemigod.services.CDUSSDService;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class Main extends Activity {

	private EditText txtCode;
	private TextView txtResult;

	private BroadcastReceiver ussdRec = new USSDReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		txtCode = (EditText) findViewById(R.id.txtCode);
		txtResult = (TextView) findViewById(R.id.txtResult);
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerReceiver(ussdRec, new IntentFilter(CDUSSDService.INTENT_USSD));

	}

	@Override
	protected void onPause() {
		super.onPause();

		unregisterReceiver(ussdRec);
	}

	public void invoke(View v) {

		if (txtCode.length() < 1) {
			txtCode.setError("Enter USSD Code");
			return;
		}

		sendBroadcast(new Intent(CDUSSDService.INTENT_ACTIVATE));

		startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:"
				+ Uri.encode("*") + txtCode.getText().toString()
				+ Uri.encode("#"))));
	}

	class USSDReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			txtResult.setText(intent.getExtras().getString("extra") + "");
		}
	}
}
