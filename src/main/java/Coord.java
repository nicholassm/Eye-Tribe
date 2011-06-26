public class Coord {
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

