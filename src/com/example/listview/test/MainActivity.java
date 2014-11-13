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
		// 启用javascript
		contentWebView.getSettings().setJavaScriptEnabled(true);
		// 从assets目录下面的加载html
		contentWebView.loadUrl("file:///android_asset/web/test1.html");

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(btnClickListener);
		contentWebView.addJavascriptInterface(this, "wst");
	}

	OnClickListener btnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// 无参数调用
			contentWebView.loadUrl("javascript:javacalljs()");
			// 传递参数调用
			contentWebView.loadUrl("javascript:javacalljswithargs(" + "'hello world'" + ")");
		}
	};
	/**4.2以后需要加这个注释， 否则无法调用*/
	@JavascriptInterface
	public void startFunction() {
		Toast.makeText(this, "js调用了java函数", Toast.LENGTH_SHORT).show();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				msgView.setText(msgView.getText() + "\njs调用了java函数");

			}
		});
	}
	@JavascriptInterface
	public void startFunction(final String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				msgView.setText(msgView.getText() + "\njs调用了java函数传递参数：" + str);

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
