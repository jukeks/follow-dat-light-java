package geometry;

public class Vector {
	double x, y, z;
	
	public Vector(Point p1, Point p2) {
		
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double magnitude() {
		return Math.sqrt(dot(this));
	}
	
	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}
	
	public Point add(Point other) {
		return new Point(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector sub(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}
	
	public Vector scale(double factor) {
		return new Vector(factor * x, factor * y, factor * z);
	}
	
	public double dot(Vector other) {
		return x * other.x + y * other.y + z * other.z;
	}
    
	public double dot(Point other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	public Vector cross(Vector other) {
        return new Vector(y * other.z - z * other.y,
			                z * other.x - x * other.z,
			                x * other.y - y * other.x);	
	}
	
	public Vector normalized() {
		return scale(1.0 / magnitude());
	}
}
