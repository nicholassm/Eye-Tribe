package com.eyetribe

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.os.Handler
import _root_.android.widget.TextView
import _root_.android.webkit._
import _root_.android.util.Log

class EyeTribeBrowser extends Activity {
  
  private var webView: WebView = null
  private val LOG_TAG     = "EyeTribeBrowser"
  private val SCROLL_STEP = 50
  private val TEST_PAGE   = "http://www.techcrunch.com"

  private var eyeGaze: EyeGaze = null

  val handler = new Handler

  val doPageDown = new Runnable { 
    def run = {
      info("Scrolling down")
      info("y=" + webView.getScrollY)
      webView.scrollTo(0, webView.getScrollY + SCROLL_STEP)
    }
  }

  val doPageUp = new Runnable { 
    def run = {
      info("Scrolling up")
      info("y=" + webView.getScrollY)
      webView.scrollTo(0, webView.getScrollY - SCROLL_STEP)
    }
  }

  def scrollDown() = handler.post(doPageDown)
  def scrollUp()   = handler.post(doPageUp)

  val gazeListener = new EyeGaze.GazeListener {
		def East(inertia: Int) {}
		def West(inertia: Int) {}
		def North(inertia: Int)     = scrollUp()
		def NorthEast(inertia: Int) = scrollUp()
		def NorthWest(inertia: Int) = scrollUp()
		def South(inertia: Int)     = scrollDown()
		def SouthEast(inertia: Int) = scrollDown()
		def SouthWest(inertia: Int) = scrollDown()
		def Center() {}
		def PageUp() {}
		def PageDown() {}
		def Back() {}
		def Dwell() {}
  }

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.eyetribebrowser)
    webView = findViewById(R.id.eyetribebrowser).asInstanceOf[WebView]
    webView.setWebViewClient(new WebViewClient())
    val webSettings = webView.getSettings();
    webSettings.setJavaScriptEnabled(true);

    eyeGaze = new EyeGaze(webView.getWidth, webView.getHeight)
    eyeGaze.addListener(gazeListener)

    webView.loadUrl(TEST_PAGE)
  }

  private def info(msg: String) {
    Log.i(LOG_TAG, "***** "+msg)
  }
}
