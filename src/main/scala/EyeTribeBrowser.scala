package com.eyetribe

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.os.Handler
import _root_.android.webkit._
import _root_.android.util.Log
import _root_.android.content.Context
import _root_.android.view.WindowManager

class EyeTribeBrowser extends Activity {
  
  private var webView: WebView = null
  private val LOG_TAG     = "EyeTribeBrowser"
  private val SCROLL_STEP = 5
  private val TEST_PAGE   = "http://www.techcrunch.com"

  private var eyeGaze: EyeGaze = null

  val handler = new Handler

  val doScrollDown = new Runnable { 
    def run = {
      info("Scrolling down")
      info("y=" + webView.getScrollY)
      webView.scrollTo(webView.getScrollX, webView.getScrollY + SCROLL_STEP)
    }
  }

  val doScrollUp = new Runnable { 
    def run = {
      info("Scrolling up")
      info("y=" + webView.getScrollY)
      webView.scrollTo(webView.getScrollX, webView.getScrollY - SCROLL_STEP)
    }
  }

  private def scrollDown() = handler.post(doScrollDown)
  private def scrollUp()   = handler.post(doScrollUp)

  val gazeListener = new EyeGaze.GazeListener {
		def East(inertia: Int) {}
		def West(inertia: Int) {}
		def North(inertia: Int)     = { info("North");     scrollUp()   }
		def NorthEast(inertia: Int) = { info("NorthEast"); scrollUp()   }
		def NorthWest(inertia: Int) = { info("NorthWest"); scrollUp()   }
		def South(inertia: Int)     = { info("South");     scrollDown() }
		def SouthEast(inertia: Int) = { info("SouthEast"); scrollDown() }
		def SouthWest(inertia: Int) = { info("SouthWest"); scrollDown() }
		def Center() = { info("Center") }
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

    info("Loading page")
    webView.loadUrl(TEST_PAGE)

    val display = getSystemService(Context.WINDOW_SERVICE).asInstanceOf[WindowManager].getDefaultDisplay(); 
    val width  = display.getWidth(); 
    val height = display.getHeight();
   
    info("Starting eyeGaze... (" + width + ", " + height + ")")
    eyeGaze = new EyeGaze(width, height)
    eyeGaze.addListener(gazeListener)
    eyeGaze.read()
  }

  override def onPause() {
    eyeGaze.pauseReading()
  }

  override def onStop() {
    eyeGaze.pauseReading()
  }
  
  override def onDestroy() {
    eyeGaze.pauseReading()
    eyeGaze.close()
  }

  private def info(msg: String) {
    Log.i(LOG_TAG, "***** "+msg)
  }
}
