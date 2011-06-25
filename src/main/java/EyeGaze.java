package com.eyetribe;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;

class Coord {
	public Coord(int _x, int _y) {
		this.x = _x;
		this.y = _y;
	}
	
	public final int x;
	public final int y;

  public String toString() {
    return "("+x+", "+y+")";
  }
}

public class EyeGaze implements UDPInput.Listener {
	int width, height;
	UDPInput udpInput;
	static int vertMargin =  12; // upper and lower margin values in %
	static int horizMargin = 30; // right and left margin values in %
	int leftMargin, rightMargin, upperMargin, lowerMargin;
	private List<GazeListener> listeners = new ArrayList<GazeListener>();
	
	public EyeGaze(int width, int height){
		udpInput = new UDPInput();
    udpInput.addListener(this);
		this.width = width;
		this.height = height;
		
		leftMargin  = (width * horizMargin) / 100;
		rightMargin = width - (width * horizMargin) / 100;
		upperMargin = (height * vertMargin) / 100;
		lowerMargin = height - (height * vertMargin) / 100;
	}
	
  public void read() {
    udpInput.read();
  }

  public void pauseReading() {
    udpInput.pauseReading();
  }

  public void close() {
    udpInput.close();
  }

  private Coord inputAsPortrait(Coord coord) {
    return new Coord(width - coord.y, coord.x);
  }

	public void handleGazeDataSet(UDPInput udpInput) {
		int x = Math.round(Float.parseFloat(udpInput.getX()));
		int y = Math.round(Float.parseFloat(udpInput.getY()));
    Coord coord = inputAsPortrait(new Coord(x, y));

    Log.i(UDPInput.LOG_TAG, coord.toString());
		
		if (coord.x < leftMargin ) {
			if(coord.y < upperMargin) {
				fireNorthWest(0);
			} 
      else if(coord.y > lowerMargin) {
				fireSouthWest(0);
			} 
      else {
				fireWest(0);
			}
		} 
    else if(coord.x > rightMargin) {
			if(coord.y < upperMargin) {
				fireNorthEast(0);
			} 
      else if(coord.y > lowerMargin) {
				fireSouthEast(0);
			}
      else {
				fireEast(0);
			}
		} else {
			if(coord.y < upperMargin) {
				fireNorth(0);
			} 
      else if(coord.y > lowerMargin) {
				fireSouth(0);
			} 
      else {
				fireCenter();
			}
		}
	}

	private void fireEast(int inertia) {
		for (GazeListener listener : listeners) {
            listener.East(inertia);
        }
	}
	
	private void fireWest(int inertia) {
		for (GazeListener listener : listeners) {
            listener.West(inertia);
        }
	}
	
	private void fireNorth(int inertia) {
		for (GazeListener listener : listeners) {
            listener.North(inertia);
        }
	}
	
	private void fireSouth(int inertia) {
		for (GazeListener listener : listeners) {
            listener.South(inertia);
        }
	}
	
	private void fireSouthWest(int inertia) {
		for (GazeListener listener : listeners) {
            listener.SouthWest(inertia);
        }
	}
	
	private void fireNorthWest(int inertia) {
		for (GazeListener listener : listeners) {
            listener.NorthWest(inertia);
        }
	}

	private void fireNorthEast(int inertia) {
		for (GazeListener listener : listeners) {
            listener.NorthEast(inertia);
        }
	}

	private void fireSouthEast(int inertia) {
		for (GazeListener listener : listeners) {
            listener.SouthEast(inertia);
        }
	}

	private void fireCenter() {
		for (GazeListener listener : listeners) {
            listener.Center();
        }
	}
	
	@SuppressWarnings("unused")
	private void firePageUp() {
		for (GazeListener listener : listeners) {
            listener.PageUp();
        }
	}

	@SuppressWarnings("unused")
	private void firePageDown() {
		for (GazeListener listener : listeners) {
            listener.PageDown();
        }
	}
	
	@SuppressWarnings("unused")
	private void fireBack() {
		for (GazeListener listener : listeners) {
            listener.Back();
        }
	}
	
	@SuppressWarnings("unused")
	private void fireDwell() {
		for (GazeListener listener : listeners) {
            listener.Dwell();
        }
	}
	
	public interface GazeListener {
		void East(int inertia);
		void West(int inertia);
		void North(int inertia);
		void South(int inertia);
		void NorthEast(int inertia);
		void SouthEast(int inertia);
		void NorthWest(int inertia);
		void SouthWest(int intertia);
		void Center();
		void PageUp();
		void PageDown();
		void Back();
		void Dwell();
    }

	  public void addListener(GazeListener listener) {
		 this.listeners.add(listener);
	  }
		 
	  public void removeListener(GazeListener listener) {
		 this.listeners.remove(listener);
	  }
}
