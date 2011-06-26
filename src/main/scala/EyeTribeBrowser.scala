package com.eyetribe

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.os.Handler
import _root_.android.webkit._
import _root_.android.util.Log
import _root_.android.content.Context
import _root_.android.view.WindowManager

class EyeTribeBrowser extends Activity {
  
  private var webView: WebView      = null
  private val LOG_TAG               = "EyeTribeBrowser"
  private val MIN_SCROLL_STEP       = 5
  private val MAX_SCROLL_STEP       = 15
  private val TEST_PAGE             = "http://www.cnn.com"
  private val MAX_SCREEN_BRIGHTNESS = 255
  private val MIN_SCREEN_BRIGHTNESS = 0 

  private var eyeGaze: EyeGaze = null

  val handler = new Handler

  private def scrollDown(amount: Int) {
    handler.post(new Runnable { 
      def run = {
        info("Scrolling down " + amount + "px Now y=" + webView.getScrollY)
        info("y=" + webView.getScrollY)
        webView.scrollTo(webView.getScrollX, webView.getScrollY + amount)
      }
    })
  }

  private def scrollUp(amount: Int) {
    handler.post(new Runnable { 
      def run = {
        val currentY = webView.getScrollY
        if(currentY - amount > 0) {
          info("Scrolling up " + amount + "px. Now y=" + currentY)
          webView.scrollTo(webView.getScrollX, currentY - amount)
        }
      }
    })
  }

  private def calcNorthSpeed(coord: Coord): Int = {
    val y = if(coord.y < 0) 0 else coord.y
    (eyeGaze.upperMargin - y) * (MAX_SCROLL_STEP - MIN_SCROLL_STEP) / eyeGaze.upperMargin + MIN_SCROLL_STEP
  }

  private def calcSouthSpeed(coord: Coord): Int = {
    val y = if(coord.y > eyeGaze.height) eyeGaze.height else coord.y
    (eyeGaze.height - y) * (MAX_SCROLL_STEP - MIN_SCROLL_STEP) / eyeGaze.lowerMargin + MIN_SCROLL_STEP
  }

  val gazeListener = new EyeGaze.GazeListener {
		def East(coord: Coord) {}
		def West(coord: Coord) {}
		def North(coord: Coord)       { info("North");     scrollUp(calcNorthSpeed(coord));   setScreenBrightness(MAX_SCREEN_BRIGHTNESS) }
		def NorthEast(coord: Coord)   { info("NorthEast"); scrollUp(calcNorthSpeed(coord));   setScreenBrightness(MAX_SCREEN_BRIGHTNESS) }
		def NorthWest(coord: Coord)   { info("NorthWest"); scrollUp(calcNorthSpeed(coord));   setScreenBrightness(MAX_SCREEN_BRIGHTNESS) }
		def South(coord: Coord)       { info("South");     scrollDown(calcSouthSpeed(coord)); setScreenBrightness(MAX_SCREEN_BRIGHTNESS) }
		def SouthEast(coord: Coord)   { info("SouthEast"); scrollDown(calcSouthSpeed(coord)); setScreenBrightness(MAX_SCREEN_BRIGHTNESS) }
		def SouthWest(coord: Coord)   { info("SouthWest"); scrollDown(calcSouthSpeed(coord)); setScreenBrightness(MAX_SCREEN_BRIGHTNESS) }
		def Center()                  { info("Center"); setScreenBrightness(MAX_SCREEN_BRIGHTNESS) }
    def notOnScreen(coord: Coord) { info("Not on screen"); setScreenBrightness(MIN_SCREEN_BRIGHTNESS) }
		def PageUp()   {}
		def PageDown() {}
		def Back()     {}
		def Dwell()    {}
  }

  def setScreenBrightness(value: Int) {
    handler.post(new Runnable {
      def run {
        if(value == MAX_SCREEN_BRIGHTNESS)
          maxBrightness()
        else
          dimBrightness()
      }
    })
  }

  private def dimBrightness() {
    val oldValue = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS)

    val newValue = Math.max(oldValue - 20, 0)
    info("Changing screen brightness from " + oldValue +  " to " + newValue)

    android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, newValue)

    val lp = getWindow.getAttributes
    lp.screenBrightness = 1.0f * newValue / 255f
    getWindow.setAttributes(lp)
  }

  private def maxBrightness() {
    val oldValue = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS)

    info("Changing screen brightness from " + oldValue +  " to " + MAX_SCREEN_BRIGHTNESS)

    android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, MAX_SCREEN_BRIGHTNESS)

    val lp = getWindow.getAttributes
    lp.screenBrightness = 255f
    getWindow.setAttributes(lp)
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
