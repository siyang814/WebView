package com.example.listview.test;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewDemo extends Activity {
    private static final String LOG_TAG = "WebViewDemo";
    private WebView mWebView;
    private Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.webviewdemo);
        mWebView = (WebView) findViewById(R.id.webview);


        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        // 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);


        mWebView.setWebChromeClient(new MyWebChromeClient());
        
        mWebView.setWebViewClient( new MyWebClinet()/*new WebViewClient(){
        	
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				//新连接在本页面打开 （不在系统浏览器打开）
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
        	
        }*/);

        // 看这里用到了 addJavascriptInterface 这就是我们的重点中的重点
        // 我们再看他的DemoJavaScriptInterface这个类。还要这个类一定要在主线程中
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");


        mWebView.loadUrl("file:///android_asset/web/test.html");
    }

    // 这是他定义由 addJavascriptInterface 提供的一个Object
    final class DemoJavaScriptInterface {
        DemoJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * 这不是呼吁界面线程。发表一个运行调用
         * loadUrl on the UI thread.
         * loadUrl在UI线程。
         */
        @JavascriptInterface
        public void clickOnAndroid() {        // 注意这里的名称。它为clickOnAndroid(),注意，注意，严重注意
            mHandler.post(new Runnable() {
                public void run() {
                    // 此处调用 HTML 中的javaScript 函数
                    mWebView.loadUrl("javascript:wave()");
                }
            });
        }
        @JavascriptInterface
        public void clickTest ( )
        {
        	Toast.makeText(WebViewDemo.this, "test = ", Toast.LENGTH_LONG).show();
        }
        
    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * 从javascript中提供了一个叫“提示框” 。这是很有用的
     * debugging your javascript.
     *  调试你的javascript。
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Log.d(LOG_TAG, message);
            
            new AlertDialog.Builder(WebViewDemo.this).setTitle("提示").setMessage(message+"\n"+url)
            .setPositiveButton("确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					new DemoJavaScriptInterface().clickOnAndroid();
					result.confirm();
				}
			})
            .create().show();
            result.confirm();
            return true;
        }

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			// TODO Auto-generated method stub
			
			Builder builder = new Builder(WebViewDemo.this); 

			builder.setTitle("删除确认"); 

			builder.setMessage(message); 

			builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() { 

			@Override 
			public void onClick(DialogInterface dialog, int which) { 

			result.confirm(); 

			} 

			}); 

			builder.setNeutralButton(android.R.string.cancel, new AlertDialog.OnClickListener() { 

			@Override 
			public void onClick(DialogInterface dialog, int which) { 

			result.cancel(); 

			} 
			}); 
			
			builder.setCancelable(false); 

			builder.create(); 

			builder.show(); 
			
			return super.onJsConfirm(view, url, message, result);
		}

		@Override
		public boolean onJsPrompt(WebView view, String url, String message,
				String defaultValue, JsPromptResult result) {
			// TODO Auto-generated method stub
			return super.onJsPrompt(view, url, message, defaultValue, result);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			// TODO Auto-generated method stub
			WebViewDemo.this.setTitle("可以用onReceivedTitle()方法修改网页标题");
			super.onReceivedTitle(view, title);
		}
        
        
        
    }
    
    class MyWebClinet extends WebViewClient
    {

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "WebViewClient.onLoadResource", Toast.LENGTH_SHORT).show();
			super.onLoadResource(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "WebViewClient.onPageFinished", Toast.LENGTH_SHORT).show(); 
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "WebViewClient.onPageStarted", Toast.LENGTH_SHORT).show(); 
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}
    	
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
                   return true;
        }       
        return super.onKeyDown(keyCode, event);       
    }   
    
}