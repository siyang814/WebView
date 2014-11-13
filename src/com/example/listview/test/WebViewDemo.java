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
        // �����һ�仰�Ǳ���ģ�����Ҫ��javaScript��Ȼ����һ�ж���ͽ�͵�
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);


        mWebView.setWebChromeClient(new MyWebChromeClient());
        
        mWebView.setWebViewClient( new MyWebClinet()/*new WebViewClient(){
        	
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				//�������ڱ�ҳ��� ������ϵͳ������򿪣�
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
        	
        }*/);

        // �������õ��� addJavascriptInterface ��������ǵ��ص��е��ص�
        // �����ٿ�����DemoJavaScriptInterface����ࡣ��Ҫ�����һ��Ҫ�����߳���
        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");


        mWebView.loadUrl("file:///android_asset/web/test.html");
    }

    // ������������ addJavascriptInterface �ṩ��һ��Object
    final class DemoJavaScriptInterface {
        DemoJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * �ⲻ�Ǻ��������̡߳�����һ�����е���
         * loadUrl on the UI thread.
         * loadUrl��UI�̡߳�
         */
        @JavascriptInterface
        public void clickOnAndroid() {        // ע����������ơ���ΪclickOnAndroid(),ע�⣬ע�⣬����ע��
            mHandler.post(new Runnable() {
                public void run() {
                    // �˴����� HTML �е�javaScript ����
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
     * ��javascript���ṩ��һ���С���ʾ�� �����Ǻ����õ�
     * debugging your javascript.
     *  �������javascript��
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Log.d(LOG_TAG, message);
            
            new AlertDialog.Builder(WebViewDemo.this).setTitle("��ʾ").setMessage(message+"\n"+url)
            .setPositiveButton("ȷ��", new OnClickListener() {
				
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

			builder.setTitle("ɾ��ȷ��"); 

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
			WebViewDemo.this.setTitle("������onReceivedTitle()�����޸���ҳ����");
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