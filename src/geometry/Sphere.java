package geometry;

import followdatlight.Color;

public class Sphere implements GeomObject {
	private Color color;
	Point p;
	double r;
	double reflectivity = 0.1;
	double transmittivity = 0.1;

	public Sphere(Point position, double radius) {
		this.p = position;
		this.r = radius;
		color = new Color(220, 0, 0);
	}
	
	public Sphere(Point position, double radius, Color color) {
		this(position, radius);
		this.color = color;
	}

	@Override
	public Double intersects(Ray ray) {
		Vector l = ray.p.sub(p);
		double a = ray.v.dot(ray.v);
		double b = 2 * ray.v.dot(l);
		double c = l.dot(l) - (r * r);
		
		double disc = b*b - 4*a*c;
		if (disc < 0) {
			return null;
		}
		
		
		double squared_disc = Math.sqrt(disc);
		double t1 = (-b + squared_disc)/(2*a);
		double t2 = (-b - squared_disc)/(2*a);
		
		// returning the nearest point
		if (t2 < t1) {
			double tmp = t1;
			t1 = t2;
			t2 = tmp;
		}
		
		if (t2 < 0) {
			return null;
		}
		
		if (t1 < 0) {
			return t2;
		}
		
		return t1;
	}


	@Override
	public Color colorAt(Point point) {
		return color;
	}

	@Override
	public Vector normal(Point point) {
		return new Vector(p, point).normalized();
	}

	@Override
	public float reflectivity() {
		return (float) reflectivity;
	}
	
	@Override
	public float transmittivity() {
		return (float) transmittivity;
	}
	
	
	
}
