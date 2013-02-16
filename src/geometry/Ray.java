package geometry;

public class Ray extends Line {
	public Ray(Point p1, Point p2) {
		super(p1, p2);
	}

	public Ray(Point p, Vector v) {
		super(p, v);
	}	
	
	public Point travel(double t) {
		return p.add(v.scale(t));
	}
}
