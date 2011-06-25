package android.morten.itu.killergazegame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.morten.itu.killergazegame.MainActivity.GameLoop;
import android.morten.itu.killergazegame.MainActivity.Panel;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class EyeGazeTester extends Activity {
	
	//Shared preferences
	public static final String SETTINGS = "settings";
	public static final String CONTROLS = "controls";
	private EyeGaze eyeGazer;
	private Paint paint;
    private TestLoop _thread;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
    }  
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	// obtain the shared preferences
    	SharedPreferences preferences = getSharedPreferences(SETTINGS, MODE_PRIVATE);
    	
    	// do the user already set the nickname?
    	if( !preferences.contains(CONTROLS) ) {
    		// if not, then start the "Settings" activity
    		startActivity( new Intent(this, Settings.class) );
    		return;
    	} else {
    		//Start the gesture application test
            setContentView(new Panel(this));
    	}
    }
    
    class TestLoop extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;
 
        public TestLoop(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }
 
        public void setRunning(boolean run) {
            _run = run;
        }
 
        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }

    
    
	//=====================================================
	// INNER PANEL CLASS
    //=====================================================
	
    public class Panel extends SurfaceView implements SurfaceHolder.Callback, AccelerometerListener, EyeGaze.GazeListner {
    	public String text;

    	public Panel(Context context) {
    		super(context);
    		getHolder().addCallback(this);
    		setFocusable(true);
    		
    		eyeGazer = new EyeGaze(getWidth(), getHeight());
    		eyeGazer.addListener(this);

    	}
    	
    	public void onDraw(Canvas canvas){
    		canvas.drawColor(Color.BLACK);
		    canvas.drawText(text, 20, 30, paint);
    		
    	}
    	
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
        }
     
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        	if (!_thread.isAlive()) {
        		_thread = new TestLoop(getHolder(), this);
                _thread.setRunning(true);
        	}
        }
     
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }

		@Override
		public void onAccelerationChanged(float x, float y, float z) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void East(int inertia) {
	    	text = "East";
		}

		@Override
		public void West(int inertia) {
			text = "West";
		}

		@Override
		public void North(int inertia) {
			text = "North";
		}

		@Override
		public void South(int inertia) {
			text = "South";
		}

		@Override
		public void NorthEast(int inertia) {
			text = "NorthEast";
		}

		@Override
		public void SouthEast(int inertia) {
			text = "SouthEast";
			
		}

		@Override
		public void NorthWest(int inertia) {
			text = "NorthWest";
			
		}

		@Override
		public void SouthWest(int intertia) {
			text = "SouthWest";
		}



		@Override
		public void Center() {
			text = "Center";
			
		}



		@Override
		public void PageUp() {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void PageDown() {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void Back() {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void Dwell() {
			// TODO Auto-generated method stub
			
		}
		
    }
     
}
