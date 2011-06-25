package com.eyetribe

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.os.Handler
import _root_.android.widget.TextView
import _root_.android.webkit._
import _root_.android.util.Log

class EyeTribeBrowser extends Activity {
  
  private var webView: WebView = null
  private val LOG_TAG = "EyeTribeBrowser"

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.eyetribebrowser)
    webView = findViewById(R.id.eyetribebrowser).asInstanceOf[WebView]
    webView.setWebViewClient(new WebViewClient())
    val webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);

    webView.loadUrl("http://www.techcrunch.com")

    var y = 0

    val handler = new Handler
    val doPageDown = new Runnable { 
      def run = {
        Log.i(LOG_TAG, "***** Doing page down")
        Log.i(LOG_TAG, "***** y=" + webView.getScrollY)
        y += 50
        webView.scrollTo(0, y)
      }
    }
    val doPageUp = new Runnable { 
      def run = {
        Log.i(LOG_TAG, "***** Doing page up")
        Log.i(LOG_TAG, "***** y=" + webView.getScrollY)
        y -= 50
        webView.scrollTo(0, y)
      }
    }
    for(i <- 1 to 10)
      handler.postDelayed(doPageDown, i * 2000 + 20000)
  }


}
