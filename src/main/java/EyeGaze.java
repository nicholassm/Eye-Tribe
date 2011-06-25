package android.morten.itu.killergazegame;

import java.util.ArrayList;
import java.util.List;

import android.morten.itu.killergazegame.UDPInput.Listener;

class Coord {
	public Coord(int _x, int _y) {
		this.x = _x;
		this.y = _y;
	}
	
	public int x;
	public int y;
}

public class EyeGaze implements Listener {
	int width, height;
	UDPInput udpInput;
	static int vertMargin = 30; // upper and lower margin values in %
	static int horizMargin = 30; // right and left margin values in %
	int leftMargin, rightMargin, upperMargin, lowerMargin;
	private List<GazeListner> listeners = new ArrayList<GazeListner>();
	
	public EyeGaze(int width, int height){
		udpInput = new UDPInput();
        udpInput.addListener(this);
		this.width = width;
		this.height = height;
		
		leftMargin = width * horizMargin;
		rightMargin = width - (width * horizMargin);
		upperMargin = height * vertMargin;
		lowerMargin = height - (height * vertMargin);
		
	}
	
	public void handleGazeDataSet(UDPInput udpInput) {
		int x = Math.round(Float.parseFloat(udpInput.getX()));
		int y = Math.round(Float.parseFloat(udpInput.getX()));
		/* filter gaze input */
		Coord coord = new Coord(x,y); //= gazeController.getCoord(_x, _y);
		
		if (coord.x < leftMargin ) {
			if (coord.y < upperMargin) {
				fireNorthWest(0);
			} else if (coord.y > lowerMargin) {
				fireSouthWest(0);
			} else {
				fireWest(0);
			}
		} else if (coord.x > rightMargin) {
			if (coord.y < upperMargin) {
				fireNorthEast(0);
			} else if (coord.y > lowerMargin) {
				fireSouthEast(0);
			} else {
				fireEast(0);
			}
		} else {
			if (coord.y < upperMargin) {
				fireNorth(0);
			} else if (coord.y > lowerMargin) {
				fireSouth(0);
			} else {
				fireCenter();
			}
		}
	}

	private void fireEast(int inertia) {
		for (GazeListner listener : listeners) {
            listener.East(inertia);
        }
	}
	
	private void fireWest(int inertia) {
		for (GazeListner listener : listeners) {
            listener.West(inertia);
        }
	}
	
	private void fireNorth(int inertia) {
		for (GazeListner listener : listeners) {
            listener.North(inertia);
        }
	}
	
	private void fireSouth(int inertia) {
		for (GazeListner listener : listeners) {
            listener.South(inertia);
        }
	}
	
	private void fireSouthWest(int inertia) {
		for (GazeListner listener : listeners) {
            listener.SouthWest(inertia);
        }
	}
	
	private void fireNorthWest(int inertia) {
		for (GazeListner listener : listeners) {
            listener.NorthWest(inertia);
        }
	}

	private void fireNorthEast(int inertia) {
		for (GazeListner listener : listeners) {
            listener.NorthEast(inertia);
        }
	}

	private void fireSouthEast(int inertia) {
		for (GazeListner listener : listeners) {
            listener.SouthEast(inertia);
        }
	}

	private void fireCenter() {
		for (GazeListner listener : listeners) {
            listener.Center();
        }
	}
	
	@SuppressWarnings("unused")
	private void firePageUp() {
		for (GazeListner listener : listeners) {
            listener.PageUp();
        }
	}

	@SuppressWarnings("unused")
	private void firePageDown() {
		for (GazeListner listener : listeners) {
            listener.PageDown();
        }
	}
	
	@SuppressWarnings("unused")
	private void fireBack() {
		for (GazeListner listener : listeners) {
            listener.Back();
        }
	}
	
	@SuppressWarnings("unused")
	private void fireDwell() {
		for (GazeListner listener : listeners) {
            listener.Dwell();
        }
	}
	
	public interface GazeListner {
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

	  public void addListener(GazeListner listener) {
		 this.listeners.add(listener);
	  }
		 
	  public void removeListener(GazeListner listener) {
		 this.listeners.remove(listener);
	  }
}
