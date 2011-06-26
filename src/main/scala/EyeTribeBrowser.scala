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
  private val LOG_TAG         = "EyeTribeBrowser"
  private val MIN_SCROLL_STEP = 5
  private val MAX_SCROLL_STEP = 15
  private val TEST_PAGE       = "http://www.techcrunch.com"

  private var eyeGaze: EyeGaze = null

  val handler = new Handler

  private def scrollDown(amount: Int) {
    handler.post(new Runnable { 
      def run = {
        info("Scrolling down")
        info("y=" + webView.getScrollY)
        webView.scrollTo(webView.getScrollX, webView.getScrollY + amount)
      }
    })
  }

  private def scrollUp(amount: Int) {
    handler.post(new Runnable { 
      def run = {
        info("Scrolling up")
        info("y=" + webView.getScrollY)
        webView.scrollTo(webView.getScrollX, webView.getScrollY - amount)
      }
    })
  }

  private def calcNorthSpeed(coord: Coord) = (if(coord.x < 0) 0 else coord.x) * (MAX_SCROLL_STEP - MIN_SCROLL_STEP) / eyeGaze.upperMargin + MIN_SCROLL_STEP
  private def calcSouthSpeed(coord: Coord) = (eyeGaze.height - (if(coord.y > eyeGaze.height) eyeGaze.height else coord.y)) * (MAX_SCROLL_STEP - MIN_SCROLL_STEP) / eyeGaze.lowerMargin + MIN_SCROLL_STEP

  val gazeListener = new EyeGaze.GazeListener {
		def East(coord: Coord) {}
		def West(coord: Coord) {}
		def North(coord: Coord)     = { info("North");     scrollUp(calcNorthSpeed(coord))   }
		def NorthEast(coord: Coord) = { info("NorthEast"); scrollUp(calcNorthSpeed(coord))   }
		def NorthWest(coord: Coord) = { info("NorthWest"); scrollUp(calcNorthSpeed(coord))   }
		def South(coord: Coord)     = { info("South");     scrollDown(calcSouthSpeed(coord)) }
		def SouthEast(coord: Coord) = { info("SouthEast"); scrollDown(calcSouthSpeed(coord)) }
		def SouthWest(coord: Coord) = { info("SouthWest"); scrollDown(calcSouthSpeed(coord)) }
		def Center() = { info("Center") }
		def PageUp()   {}
		def PageDown() {}
		def Back()     {}
		def Dwell()    {}
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
