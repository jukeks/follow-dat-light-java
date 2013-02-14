package objects;

public class Point {
	double x, y, z;
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point add(Vector other) {
		return other.add(this);
	}
	
	public Vector sub(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}
	
	public Point sub(Point other) {
		return new Point(x - other.x, y - other.y, z - other.z);
	}
}
