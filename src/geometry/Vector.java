package geometry;

public class Vector {
	double x, y, z;
	
	public Vector(Point p1, Point p2) {
		this(p2.sub(p1));
	}
	
	private Vector(Vector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector reflect(Vector normal) {
		normal = normal.normalized();
        double d = 2 * (x * normal.x + y * normal.y + z * z);
        return new Vector(x + d * normal.x,
                       		y + d * normal.y,
                       		z + d * normal.z);

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
	
	public Vector sub(Point other) {
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
	
	public Vector cross(Point other) {
        return new Vector(y * other.z - z * other.y,
			                z * other.x - x * other.z,
			                x * other.y - y * other.x);	
	}
	
	public Vector normalized() {
		return scale(1.0 / magnitude());
	}
}
