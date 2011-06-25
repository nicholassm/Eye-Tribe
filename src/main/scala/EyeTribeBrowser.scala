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

    info("Starting eyeGaze...")
    eyeGaze = new EyeGaze(webView.getWidth, webView.getHeight)
    eyeGaze.addListener(gazeListener)
    eyeGaze.read()

    info("Loading page")
    webView.loadUrl(TEST_PAGE)
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
