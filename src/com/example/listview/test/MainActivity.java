package com.example.listview.test;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private WebView contentWebView = null;
	private TextView msgView = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		contentWebView = (WebView) findViewById(R.id.webview);
		msgView = (TextView) findViewById(R.id.msg);
		// ����javascript
		contentWebView.getSettings().setJavaScriptEnabled(true);
		// ��assetsĿ¼����ļ���html
		contentWebView.loadUrl("file:///android_asset/web/test1.html");

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(btnClickListener);
		contentWebView.addJavascriptInterface(this, "wst");
	}

	OnClickListener btnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// �޲�������
			contentWebView.loadUrl("javascript:javacalljs()");
			// ���ݲ�������
			contentWebView.loadUrl("javascript:javacalljswithargs(" + "'hello world'" + ")");
		}
	};
	/**4.2�Ժ���Ҫ�����ע�ͣ� �����޷�����*/
	@JavascriptInterface
	public void startFunction() {
		Toast.makeText(this, "js������java����", Toast.LENGTH_SHORT).show();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				msgView.setText(msgView.getText() + "\njs������java����");

			}
		});
	}
	@JavascriptInterface
	public void startFunction(final String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				msgView.setText(msgView.getText() + "\njs������java�������ݲ�����" + str);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		startActivity( new Intent(this, WebViewDemo.class));
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		startActivity( new Intent(this, WebViewDemo.class));
		return super.onOptionsItemSelected(item);
	}

}
