package geometry;

public class Line {
	public Point p;
	public Vector v;
	
	public Line(Point p, Vector v) {
		this.p = p;
		this.v = v;
	}
	
	public Line(Point p1, Point p2) {
		this.p = p1;
		this.v = p2.sub(p1);
	}
}
