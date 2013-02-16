package geometry;

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
	
	public double distance(Point p) {
		double dx = p.x - x;
		double dy = p.y - y;
		double dz = p.z - z;
		
		return Math.sqrt(dx*dx+dy*dy+dz*dz); 
	}
	
	public Vector add(Point other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector sub(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}
	
	public Vector sub(Point other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}
	
	public double dot(Vector other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	public double dot(Point other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	
	public boolean equals(Point other) {
		if (other == null) {
			return false;
		}
		if (Double.compare(x, other.x) != 0) {
			return false;
		}
		if (Double.compare(y, other.y) != 0) {
			return false;
		}
		if (Double.compare(z, other.z) != 0) {
			return false;
		}
		
		return true;
	}
}
