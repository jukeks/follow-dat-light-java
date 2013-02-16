package geometry;

import followdatlight.Color;



public class Plane implements GeomObject {
	Color base = new Color(0, 0, 0);
	Color other = new Color(255, 255, 255);
	Vector n;
	double k;
	double reflectivity = 0.1;
	double transmittivity = 0.1;
	
	public Plane(Point p, Vector v) {
        n = v.normalized();
        k = n.dot(p);
	}

	@Override
	public Point intersects(Ray ray) {
		double d = ray.v.dot(n);
		if (d == 0) {
			return null;
		}
		
		double t = -(ray.p.dot(n) + k) / d;
		if (t < 0) {
			return null;
		}
		
	    return ray.travel(t);	
	}

	@Override
	public Color colorAt(Point point) {
        if ((((int)(Math.abs(point.x) + 0.5)) + ((int)(Math.abs(point.y) + 0.5)) + ((int)(Math.abs(point.z) + 0.5))) % 2 != 0) {
        	return base;
        } else {
        	return other;
        }
	}

	@Override
	public Vector normal(Point point) {
		return n;
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
